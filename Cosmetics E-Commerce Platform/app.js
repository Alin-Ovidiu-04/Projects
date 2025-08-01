
const session = require('express-session');

const express = require('express');
const expressLayouts = require('express-ejs-layouts');
const bodyParser = require('body-parser');
const cookieParser = require('cookie-parser');
const app = express();
const port = 6789;
app.use(cookieParser());
const fs = require('fs');
var listaIntrebari;
var listaUtilizatori;

let produseCosmetice = [
  ['Fond de ten', '30 ml'],
  ['Ruj', '4 g'],
  ['Mascara', '10 ml'],
  ['Pudră compactă', '15 g'],
  ['Creion de buze', '1.2 g'],
  ['Fard de obraz', '6 g'],
  ['Rimel', '8 ml']
];

const sqlite3 = require('sqlite3').verbose();


fs.readFile('intrebari.json', (err, data) => {
    if (err) throw err;
    listaIntrebari = JSON.parse(data).intrebari;
});

fs.readFile('utilizatori.json', (err, data) => {
    if (err) throw err;
    listaUtilizatori = JSON.parse(data).utilizatori;
});


app.use(session({
    secret: 'secret-key',
    resave: false,
    saveUninitialized: false,
    cookie: {
      // Configurați cookie-ul pentru sesiune
      maxAge: 60 * 60 * 1000 // Timp de expirare: 1 ora
    }
  }));

// directorul 'views' va conține fișierele .ejs (html + js executat la server)
app.set('view engine', 'ejs');
// suport pentru layout-uri - implicit fișierul care reprezintă template-ul site-ului este views/layout.ejs
app.use(expressLayouts);
// directorul 'public' va conține toate resursele accesibile direct de către client (e.g., fișiere css, javascript, imagini)
app.use(express.static('public'))
// corpul mesajului poate fi interpretat ca json; datele de la formular se găsesc înformat json în req.body
app.use(bodyParser.json());
// utilizarea unui algoritm de deep parsing care suportă obiecte în obiecte
app.use(bodyParser.urlencoded({ extended: true }));
// la accesarea din browser adresei http://localhost:6789/ se va returna textul 'Hello World'
// proprietățile obiectului Request - req - https://expressjs.com/en/api.html#req
// proprietățile obiectului Response - res - https://expressjs.com/en/api.html#res


app.get('/', (req, res) => {
    const utilizator = req.session.utilizator;
    const produseCosmetice = req.session.produseCosmetice;
    res.render('index', { utilizator, produseCosmetice});
});

// la accesarea din browser adresei http://localhost:6789/chestionar se va apela funcția specificată
app.get('/chestionar', (req, res) => {
  const utilizator = req.session.utilizator;
 // în fișierul views/chestionar.ejs este accesibilă variabila 'intrebari' care conține vectorul de întrebări
 res.render('chestionar', {intrebari: listaIntrebari, utilizator});
});

app.get('/autentificare', (req, res) => {

    const mesajEroare = req.session.mesajEroare; // Obține cookie-ul "mesajEroare"
    res.render('autentificare', { mesajEroare }); // Pasează mesajul de eroare către view-ul autentificare
});

app.post('/rezultat-chestionar', (req, res) => {
  const utilizator = req.session.utilizator;
 res.render('rezultat-chestionar', {intrebari: listaIntrebari,raspunsuri: req.body, utilizator});
});

app.post('/verificare-autentificare', (req, res) => {

  let Valid = 0;

    listaUtilizatori.forEach(function(utilizator) {
        const numeCorect = utilizator.user;
        const parolaCorecta = utilizator.password;
        const numePrimit = req.body.nume;
        const parolaPrimita = req.body.parola;
        if (numePrimit === numeCorect && parolaPrimita === parolaCorecta) {
            Valid = 1;

            req.session.utilizator = {
              nume: utilizator.user,
              email: utilizator.email,
              rol: utilizator.rol
            };

        }
    });

    if (Valid === 1) {

        console.log(req.session.utilizator);
        res.redirect(303, 'http://localhost:6789/');
      } else {
        req.session.mesajEroare = 'Date invalide!';
        res.redirect(303, 'http://localhost:6789/autentificare');
      }
});

app.post('/delogare', (req, res) => {

    req.session.destroy(err => {
      if (err) {
        console.log(err);
      }

      res.clearCookie('sessionId');
      res.clearCookie('utilizator');
      res.redirect(303, 'http://localhost:6789/');
    });

});

app.get('/creare-bd', (req, res) => {
  let db = new sqlite3.Database('Cumparaturi.db', (err) => {
    if (err) {
      return console.error(err.message);
    }
    console.log('Connected to the in-memory SQlite Cumparaturi D atabase.');

    db.run('CREATE TABLE IF NOT EXISTS Produse (id INTEGER PRIMARY KEY AUTOINCREMENT, denumire TEXT, gramaj TEXT)', (err) => {
      if (err) {
          console.error('Eroare la crearea tabelei', err.message);
      } else {
          console.log('Tabela "Produse" a fost creată !');
      }
  });

  });

  db.close();
  res.redirect(303, 'http://localhost:6789/');

});

app.get('/inserare-bd', (req, res) => {

  const db = new sqlite3.Database('cumparaturi.db', (err) => {
    if (err) {
        console.error(err.message);
        return;
    }
    console.log('Conexiune realizată cu succes la baza de date SQLite (pentru inserare).');

    let sqlBase = 'INSERT INTO produse (denumire, gramaj) VALUES';
    produseCosmetice.forEach((produs, index) => {
        let sql = sqlBase + '(?, ?)';
        db.run(sql, produs, (err) => {
            if (err) {
                if (err.errno === 19) {
                    console.log('Inregistrarea există deja.');
                } else {
                    console.log('Eroare la inserare:', err.message);
                }
            } else {
                console.log('Inserare cu succes.');
            }
            if (index === produseCosmetice.length - 1) {
                db.close();
            }
        });
    });
});

res.redirect(303, 'http://localhost:6789/');

});

app.get('/afisare-produse', (req, res) => {

  const db = new sqlite3.Database('cumparaturi.db', (err) => {
      if (err) {
          console.error(err.message);
          return;
      }
      console.log('Conexiune realizată cu succes la baza de date SQLite (pentru afișare produse).');

      let sql = 'SELECT * FROM Produse';
      db.all(sql, [], (err, rows) => {
          if (err) {
              console.error('Eroare la extragerea datelor:', err.message);
          } else {
              console.log('Date extrase cu succes.');
              req.session.produseCosmetice = rows;
              console.log(req.session.produseCosmetice);
              console.log(req.session.utilizator);
              db.close();
              res.redirect(303, 'http://localhost:6789/');
          }

      });
  });

});

app.post('/adaugare-cos', (req, res) => {

  if (req.session.cumparaturi === undefined) {
      req.session.cumparaturi = [];
  }

  console.log(req.session.produseCosmetice);
  req.session.produseCosmetice.forEach((produs) => {
      if (produs.id == req.body.id) {
          let status = false;
          req.session.cumparaturi.forEach((Product) => {
              if (Product.id == produs.id) {
                  status = true;
              }
          });

          if (status === false) {
              req.session.cumparaturi.push(produs);
          }
      }
  });

  console.log(req.session.cumparaturi);
  res.redirect(303, 'http://localhost:6789/');

});

app.get('/vizualizare-cos', (req, res) => {
    res.render('vizualizare-cos', { cumparaturi: req.session.cumparaturi ,utilizator : req.session.utilizator});
});

app.get('/sterge-produse', (req, res) => {
    const db = new sqlite3.Database('cumparaturi.db', sqlite3.OPEN_READWRITE, (err) => {
      if (err) {
        console.error(err.message);
      } else {
        const deleteSql = "DELETE FROM produse";
        db.run(deleteSql, [], function (err) {
          if (err) {
            console.error(err.message);
          } else {
            console.log('Prosude sterse!');
  
            const resetIdSql = "DELETE FROM sqlite_sequence WHERE name='Produse';"; // Resetează valoarea secvenței
            db.run(resetIdSql, [], function (err) {
              if (err) {
                console.error(err.message);
              } else {
                console.log("Resetare autoincrement realizata!");
                res.redirect(303, 'http://localhost:6789/');
              }
            });
          }
        });
      }
    });
});

app.get('/admin', (req, res) => {

  res.render('admin', { utilizator: req.session.utilizator});

});

app.post('/adauga-produs', (req, res) => {

  const db = new sqlite3.Database('cumparaturi.db', (err) => {
    if (err) {
        console.error(err.message);
        return;
    }
    console.log('Conexiune realizată cu succes la baza de date SQLite (pentru inserare).');

    console.log(req.body);
    let sqlBase = 'INSERT INTO Produse VALUES((SELECT COUNT(*) + 1 FROM Produse), "' + req.body.denumire + '", "' + req.body.gramaj +'")';
    
    db.run(sqlBase, (err) => {
          if (err) {
              console.error('Eroare la adaugare produs', err.message);
          } else {
              console.log('Produs adaugat cu succes!');
          }
      });
});

res.redirect(303, 'http://localhost:6789/');

});

const accesuriInexistente = {};
var permited = 4;


// Middleware pentru a verifica accesul
app.use((req, res, next) => {
  const { ip, path } = req;
  console.log(ip);
  console.log(path);
  // Verificăm dacă ruta solicitată nu există în directorul "public"
  if (!path.startsWith('/public/') && !fs.existsSync(`public${path}`)) {
    console.log("afara");
    // Verificăm dacă există înregistrări pentru IP-ul curent
    if (accesuriInexistente[ip]) {
      const accesuri = accesuriInexistente[ip];

      // Verificăm dacă utilizatorul/IP-ul a efectuat prea multe încercări
      if (accesuri.length >= permited) {
        const ultimaIncercare = accesuri[accesuri.length - 1];
        const timpScurs = Math.floor((Date.now() - ultimaIncercare) / 1000); // Calculăm timpul scurs în secunde

        // Dacă au trecut mai puțin de 60 de secunde, blocăm accesul
        if (timpScurs < 60) {
          return res.status(429).send('Accesul la resurse este blocat temporar. Vă rugăm să încercați din nou mai târziu.');
        }

        // Dacă a trecut mai mult de 60 de secunde, eliminăm înregistrările vechi
        accesuriInexistente[ip] = accesuri.filter((acces) => Math.floor((Date.now() - acces) / 1000) < 60);
      }
    }

    // Adăugăm înregistrarea pentru utilizatorul/IP-ul curent
    if (!accesuriInexistente[ip]) {
      accesuriInexistente[ip] = [];
    }
    accesuriInexistente[ip].push(Date.now());
  }

  next();
});

app.listen(port, () => console.log(`Serverul rulează la adresa http://localhost:`));