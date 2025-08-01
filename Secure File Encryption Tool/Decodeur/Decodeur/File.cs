using System;
using System.Collections.Generic;
using System.Linq;
using System.Text;
using System.Threading.Tasks;

namespace Decodeur
{
    internal class File
    {
        private int _id;
        private string _name;
        private bool _isDecrypted;
        private int _idAlg;
        private int _idKey;

        public int Id { get { return _id; } }
        public string Name { get => _name; set => _name = value; }
        public bool IsDecrypted { get => _isDecrypted; set => _isDecrypted = value; }
        public int IdAlg { get => _idAlg; set => _idAlg = value; }
        public int IdKey { get => _idKey; set => _idKey = value; }

        public File(int id, string name, bool isDecrypted, int idAlg, int idKey)
        {
            _id = id;
            _name = name;
            _isDecrypted = isDecrypted;
            _idAlg = idAlg;
            _idKey = idKey;
        }
    }
}