using System;
using System.Collections.Generic;
using System.Linq;
using System.Text;
using System.Threading.Tasks;

namespace Decodeur
{
    internal class Algorithm
    {
        private int _id;
        private string _name;
        private string _type;

        public int Id { get { return _id; } }
        public string Name { get => _name; set => _name = value; }
        public string Type { get => _type; set => _type = value; }

        public Algorithm(int id, string name, string type)
        {
            _id = id;
            _name = name;
            _type = type;
        }
    }
}