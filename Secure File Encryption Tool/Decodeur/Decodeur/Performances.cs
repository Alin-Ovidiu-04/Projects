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
    public partial class Performances : Form
    {
        Dictionary<int,Performance> performancesDic;

        public Performances()
        {
            InitializeComponent();

            performancesDic = Database.GetPerformances();

            foreach (var kvp in performancesDic)
            {
                comboBox1.Items.Add(Database.GetFile(kvp.Value.IdFile));
            }

            comboBox1.SelectedIndexChanged += new EventHandler(comboBox1_SelectedIndexChanged);
        }

        private void comboBox1_SelectedIndexChanged(object sender, EventArgs e)
        {
            
            if (comboBox1.SelectedIndex != -1)
            {
                foreach (var kvp in performancesDic)
                {
                    if(Database.GetFile(kvp.Value.IdFile) == comboBox1.Text)
                    {
                        textBox1.Text = kvp.Value.TimeInMs.ToString();
                        textBox2.Text = kvp.Value.Type;
                        textBox3.Text = Database.GetFrameworkName(kvp.Value.IdFramework);
                    }
                }
            }
        }

        private void Back_Click(object sender, EventArgs e)
        {
            this.Close();
        }
    }
}
