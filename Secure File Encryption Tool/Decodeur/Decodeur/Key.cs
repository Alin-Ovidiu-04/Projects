using System;
using System.Collections.Generic;
using System.Data.SqlClient;
using System.Linq;
using System.Text;
using System.Threading.Tasks;

namespace Decodeur
{
    internal class Key
    {
        private int _id;
        private string _encryptKey;
        private string _decryptKey;

        public int Id { get { return _id; } }
        public string EncryptKey { get => _encryptKey; set => _encryptKey = value; }
        public string DecryptKey { get => _decryptKey; set => _decryptKey = value; }

        public Key(int id, string encryptKey, string decryptKey)
        {
            _id = id;
            _encryptKey = encryptKey;
            _decryptKey = decryptKey;
        }
    }
}