<style>

  .v-input__control, .v-input__slot, .v-select__slot {
		border: 0;
	}

  .custom.v-text-field>.v-input__control>.v-input__slot:before {
    border-style: none;
  }

  .custom.v-text-field>.v-input__control>.v-input__slot:after {
    border-style: none;
  }

  #titleText {
    color: white;
    text-decoration: none;
  }

  #title {
    padding-top: 2px;
  }

</style>

<template>
  <v-app>
    <v-app-bar app dark color="green">
      <v-toolbar-title id="title" class="headline text-uppercase">
          <span class="font-weight-light">
            <router-link to='/'>
          </router-link>
          <router-link to='/' id="titleText">&nbsp;&nbsp;&nbsp;Engie Dashboard</router-link>
          </span>
      </v-toolbar-title>
      <v-spacer></v-spacer>
      <v-btn text icon @click="showDatePicker = true" title ="Select Date Range">
        <v-icon>mdi-calendar-month</v-icon>
      </v-btn>
      <v-btn text to="/" exact>Dashboard</v-btn>
      <!-- <v-btn text to="/buildings" exact>Buildings</v-btn> -->
    </v-app-bar>
      <v-content>
        <router-view/>
    </v-content>
    <v-container grid-list-md text-xs-center>
      <v-layout wrap>
        <v-flex xs6>
          <v-dialog v-model="showDatePicker" width="750">
            <v-card>
              <v-card-title class="headline grey lighten-2" primary-title>
                Select Date Range
              </v-card-title>
              <br />
              <v-layout wrap justify-space-around>
                <v-flex xs12 sm6 md4>
                  <v-menu
                    transition="scale-transition"
                    offset-y
                    full-width
                    min-width="290px"
                    >
                    <template v-slot:activator="{ on }">
                      <v-text-field
                        v-model="start"
                        label="Start Date"
                        prepend-icon="event"
                        readonly
                        v-on="on"
                      ></v-text-field>
                    </template>
                    <v-date-picker v-model="start" :max="end"></v-date-picker>
                  </v-menu>
                </v-flex>
                <v-flex xs12 sm6 md4>
                  <v-menu
                    :nudge-right="40"
                    transition="scale-transition"
                    offset-y
                    full-width
                    min-width="290px"
                  >
                    <template v-slot:activator="{ on }">
                      <v-text-field
                        v-model="end"
                        label="End Date"
                        prepend-icon="event"
                        readonly
                        v-on="on"
                      ></v-text-field>
                    </template>
                    <v-date-picker v-model="end" :min="start"></v-date-picker>
                  </v-menu>
                </v-flex>
              </v-layout>
              <br />
              <v-divider></v-divider>
              <v-card-actions>
                <v-btn color="primary" text :disabled="isDisabledApply" v-on:click="dateChosen">Apply</v-btn>
              </v-card-actions>
            </v-card>
          </v-dialog>
        </v-flex>
      </v-layout>
    </v-container>
  </v-app>
</template>

<script>
export default {

  name: "App",
  data: () => ({
    end: '',
    start: '',
    showDatePicker: false
  }),

  methods:
  {
    dateChosen: function()
    {
      this.showDatePicker = false;
      this.$store.commit('selectDate', {"start": this.start, "end":this.end});
      this.$store.dispatch('setDate');
    }
  },

  created()
  {
    this.$store.dispatch('initialize');
  },

 
  computed: {
    isDisabledApply()
    {
      return ((this.start == '') || (this.end == ''));
    }
  }

};
</script>
