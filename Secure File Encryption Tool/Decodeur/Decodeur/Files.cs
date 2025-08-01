using System;
using System.Collections.Generic;
using System.ComponentModel;
using System.Data;
using System.Drawing;
using System.IO;
using System.Linq;
using System.Text;
using System.Threading.Tasks;
using System.Windows.Forms;

namespace Decodeur
{
    public partial class Files : Form
    {
        public Files()
        {
            InitializeComponent();

            //string folderPath = Path.Combine(AppDomain.CurrentDomain.BaseDirectory, "Files"); // Path către folderul "FILES" din cadrul proiectului
            
            //if (Directory.Exists(folderPath))
            //{
            //    string[] txtFiles = Directory.GetFiles(folderPath, "*.txt"); // Obținem toate fișierele .txt din folder

            //    foreach (string file in txtFiles)
            //    {
            //        comboBox1.Items.Add(Path.GetFileName(file)); // Adăugăm numele fișierelor în ComboBox
            //    }
            //}
        }

        private void button1_Click(object sender, EventArgs e)
        {
            // adaugarea noului folder in baza de date (TO DO)

            String name = @textBox1.Text;
            String file = Path.GetFileName(name);
            
            Database.AddFile(new File(4,file,false,1,1));
            this.Close();
        }

        private void comboBox1_SelectedIndexChanged(object sender, EventArgs e)
        {
            
        }

        private void button2_Click(object sender, EventArgs e)
        {
            this.Close();
        }

        private void button3_Click(object sender, EventArgs e)
        {
            OpenFileDialog openFileDialog = new OpenFileDialog();
            openFileDialog.Filter = "Text Files (*.txt)|*.txt|All Files (*.*)|*.*";
            openFileDialog.Title = "Select a text file";

            if (openFileDialog.ShowDialog() == DialogResult.OK)
            {

                string selectedFilePath = openFileDialog.FileName;
                textBox1.Text = selectedFilePath;
            }
        }
    }
}
