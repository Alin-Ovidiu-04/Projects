using System;
using System.Collections.Generic;
using System.ComponentModel;
using System.Data;
using System.Drawing;
using System.Linq;
using System.Text;
using System.Threading.Tasks;
using System.Windows.Forms;

namespace Decodeur
{
    public partial class Form1 : Form
    {
        public Form1()
        {
            InitializeComponent();
        }

        private void button2_Click(object sender, EventArgs e)
        {
            this.Hide();

            Performances performance = new Performances();

            performance.ShowDialog();

            this.Show();
        }

        private void button1_Click(object sender, EventArgs e)
        {
            this.Hide();

            Files file = new Files();

            file.ShowDialog();

            this.Show();
        }

        private void button3_Click(object sender, EventArgs e)
        {
            this.Hide();

            Encrypt_Decrypt file = new Encrypt_Decrypt();

            file.ShowDialog();

            this.Show();
        }
    }
}
