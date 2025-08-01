using System;
using System.Collections.Generic;
using System.Linq;
using System.Runtime.InteropServices;
using System.Text;
using System.Threading.Tasks;

namespace Decodeur
{
    internal class Performance
    {
        private int _id;
        private int _idFile;
        private int _idFramework;
        private int _timeInMs;
        private string _type;

        public int Id { get { return _id; } }
        public int IdFile { get { return _idFile; } }
        public int IdFramework { get { return _idFramework; } }
        public int TimeInMs { get { return _timeInMs; } }
        public string Type { get { return _type; } }

        public Performance(int id, int idFile, int timeInMs, int idFramework, string type)
        {
            _id = id;
            _idFile = idFile;
            _idFramework = idFramework;
            _timeInMs = timeInMs;
            _type = type;
        }
    }
}


