//Josh Pitts
//CPT 206
//Lab 3

using System;
using System.Collections.Generic;
using System.Data.SqlClient;
using System.Linq;
using System.Text;
using System.Threading.Tasks;

namespace J_Pitts_Lab_3
{
    //to create the state class for use in program
    public class State
    {
        public int StateID { get; set; }
        public string StateName { get; set; }
        public long Population { get; set; }
        public string FlagDescription { get; set; }
        public string StateFlower { get; set; }
        public string StateBird { get; set; }
        public string StateColors { get; set; }
        public string LargestCities { get; set; }
        public string Capital { get; set; }
        public decimal MedianIncome { get; set; }
        public decimal ComputerJobPercent { get; set; }
    }
        
}
