<style>

.top_card {
    padding-top: 1em;
  }

  .apexcharts-legend {
    padding-top: 80px;
}
  .top {
    padding-top: 5em;
  }

</style>

<template>
  <v-app :style="{ 'background-color': '#E0E0E0'}">
    <v-container>
      <div v-if="loading" class="top">
        <Loading/>
      </div>
      <div v-else-if="initFinished">
        <v-row class="top_card">
          <v-col cols="12">
            <v-card class="pa-6" outlined min-height="90px">
              <div style="font-size: 30px;" align="center" color="#000000">
                  <div v-if="noRange">
                    <h3>Energy Usage Overview for {{date}}</h3>
                  </div>
                  <div v-else>
                    <h3>Energy Usage Overview for {{startDate}} to {{endDate}}</h3>
                  </div>
              </div>
            </v-card>
          </v-col>
        </v-row>

        <v-row>
            <v-col cols="6">
              <div v-if="noRange">
              <v-row
                align="center"
                justify="space-around"
                no-gutters
              >
                
                  <v-card class="pa-6" outlined min-height="220px" color="lime lighten-3" min-width="180px">
                    <v-card-text style="font-size: 22px;" align="center">
                      Day
                      <br>
                      <br><br>
                      <p style="font-size: 29px;">{{daily}}<br></p>kWh
                    </v-card-text>
                  </v-card>
                  <v-card class="pa-6" outlined min-height="220px" min-width="180px" color="light-green lighten-3"> 
                    <v-card-text style="font-size: 22px;" align="center">
                      Week
                      <br><br><br>
                      <p style="font-size: 29px;">{{weekly}}<br></p>kWh
                    </v-card-text>
                  </v-card>
                  <v-card class="pa-6" outlined min-height="220px" min-width="180px" color="green lighten-3">
                    <v-card-text style="font-size: 22px;" align="center">
                      Month
                      <br><br><br>
                      <p style="font-size: 29px;">{{monthly}}<br></p>kWh
                    </v-card-text>
                  </v-card>
                  </v-row>
                </div>

                
                <div v-else>
                  <v-row
                align="center"
                justify="space-around"
                no-gutters
              >
                  <v-card class="pa-6" outlined min-height="220px" min-width="560px" color="green lighten-3">
                    <v-card-text style="font-size: 22px;" align="center">
                      {{startDate}} to {{endDate}}
                      <br><br><br>
                      <p style="font-size: 29px;">{{calcRange}}<br></p>kWh
                    </v-card-text>
                  </v-card>
                  </v-row>
                </div>
            

            <div class="top_card"></div>
            <v-card class="pa-6" outlined min-height="260px">
              <v-card-text align="center" style="font-size: 23px" color="#00001">
                <h3>Energy Usage (kWh) Over Time</h3><br>
              </v-card-text>
              <apexchart type=line height=350 :options="line_options" :series="line_series" />
            </v-card>

          </v-col>
            
          <v-col cols="6">
            <v-card class="pa-6" outlined min-height="640px">
              <br />
              <v-card-text align="center" style="font-size: 25px" color="#00001">
                <h2>Breakdown of Energy</h2><br><h2>Usage</h2><br>
              </v-card-text>
                <apexchart type=donut width=500 :options="chartOptions" :series="getPiePower" />
            </v-card>
          </v-col>

          </v-row>
        </div>
    </v-container>
    
  </v-app>
</template>

<script>
import Loading from "./Loading.vue";
import VueApexCharts from 'vue-apexcharts';
import store from "../store.js";
export default {

  components: {
    Loading,
    apexchart: VueApexCharts,
    store
  },

  data: () => ({

    chartOptions: {
      tooltip: {enabled:true},
      colors:["#E57373", "#536DFE","#90CAF9", "#FFF176"],
      labels: ["Heating Water", "Cooling Water","Steam", "Electricity"],
      dataLabels: {
        enabled: false
      },
      legend: {
        position: "top",
        fontSize: '17px',
        itemMargin: {
        horizontal: 20,
        vertical: 5
      },
      onItemHover: {
        highlightDataSeries: true,
      },
      responsive: [{
        breakpoint: 480,
        options: {
          chart: {
            width: 200
          }
        }
      }]
    }  
  }
  }),

  computed: {
    loading() {
      return this.$store.state.loading;
    },
    date() {
      return this.$store.state.date;
    },
    initFinished() {
      
      return this.$store.state.initFinished;
    },
    daily()
    {
      return this.$store.state.daily;
    },
    weekly()
    {
      return this.$store.state.weekly;
    },
    monthly()
    {
      return this.$store.state.monthly;
    },
    getPiePower()
    {
      return this.$store.state.pie_power;
    },
    noRange()
    {
      return this.$store.state.noRange;
    },
    startDate()
    {
      return this.$store.state.startDate;
    },
    endDate()
    {
      return this.$store.state.endDate;
    },
    calcRange()
    {
      return this.$store.state.calcRange;
    },
    line_series()
    {
      return ( [{
        name: 'Energy Usage (kWh)',
        data: this.$store.state.line_data
      }]);
    },
    line_options()
    {
      return ({
      chart: {
        type: 'line',
        shadow: {
          enabled: false,
          color: '#bbb',
          top: 3,
          left: 2,
          blur: 3,
          opacity: 1
        },
      },
      stroke: {
        width: 7,
        curve: 'smooth'
      },
      xaxis: {
        type: 'datetime',
        categories: this.$store.state.line_date
      },
      fill: {
        type: 'gradient',
        gradient: {
          shade: 'dark',
          gradientToColors: ['#FDD835'],
          shadeIntensity: 1,
          type: 'horizontal',
          opacityFrom: 1,
          opacityTo: 1,
          stops: [0, 100, 100, 100]
        },
      },
      markers: {
        size: 4,
        opacity: 0.9,
        colors: ["#FFA41B"],
        strokeColor: "#fff",
        strokeWidth: 2,
          
        hover: {
          size: 7,
        }
      },
      yaxis: {
        min: this.$store.state.min,
        max: this.$store.state.max,
        title: {
          text: 'Energy Usage (kWh)',
        }
      }
    });
    }
  }

};
</script>