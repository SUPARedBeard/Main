//Josh Pitts
//CPT-185
//Final Project

using System;
using System.Collections.Generic;
using System.ComponentModel;
using System.Data;
using System.Drawing;
using System.Linq;
using System.Text;
using System.Threading.Tasks;
using System.Windows.Forms;

namespace J_Pitts_CPT_185_Final_Project
{
    public partial class instructionForm : Form
    {
        public instructionForm()
        {
            InitializeComponent();
        }

        

        private void buttonStart_Click(object sender, EventArgs e)
        {
            //closes the insructions form
            this.Close();
        }
    }
}
