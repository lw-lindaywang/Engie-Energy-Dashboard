import Vue from "vue";
import Vuex from "vuex";

Vue.use(Vuex);

import axios from "axios";
export default new Vuex.Store({
  state:
  {
    dateSelected: false,
    startDate: '',
    endDate: '',
    loading: false,
    noRange: true,
    monthly: 0.0,
    date: '2017-11-03',
    weekly: 0.0,
    daily: 0.0,
    line_data: [],
    line_date: [],
    pie_power: [],
    max: 0.0,
    min: 0.0,
    calcRange: 0.0
  },

  actions:
  {

    // async refresh({ commit })
    // {

    //   commit('changeInitFinished', true);
    //   commit('changeLoading', false);

    // },

    async initialize({commit})
    {
      commit('changeLoading', true);

      await axios.get('/init');
      await axios.get('/monthBeforeSum/?start=2017-11-03')
        .then(response => {
          commit("updateMonthly", response.data);
      })

      await axios.get('/date/?&start=2017-10-27&end=2017-11-03')
        .then(response => {
          commit("updateWeekly", response.data);
      })

      await axios.get('/date/?&start=2017-11-03&end=2017-11-03')
        .then(response => {
          commit("updateDaily", response.data);
      })

      await axios.get('/weekBeforeData/?&start=2017-11-03')
        .then(response => {
          commit("updateLineData", response.data);
      })

      await axios.get('/weekBeforeDate/?&start=2017-11-03')
        .then(response => {
          commit("updateLineDate", response.data);
      })

      commit('changeInitFinished', true);
      commit('changeLoading', false);
    },

    async setDate({ commit })
    {
      commit('changeLoading', true);
      commit('setNoRange', false);

      if (this.state.startDate == this.state.endDate)
      {
        this.commit('setNoRange', true);

        await axios.get('/monthBeforeSum/?start=2017-11-03')
          .then(response => {
            commit("updateMonthly", response.data);
        })

        await axios.get('/date/?&start=2017-10-27&end=2017-11-03')
          .then(response => {
            commit("updateWeekly", response.data);
        })

        await axios.get('/date/?&start=2017-11-03&end=2017-11-03')
          .then(response => {
            commit("updateDaily", response.data);
        })

        await axios.get('/weekBeforeData/?&start=2017-11-03')
          .then(response => {
            commit("updateLineData", response.data);
        })

        await axios.get('/weekBeforeDate/?&start=2017-11-03')
          .then(response => {
            commit("updateLineDate", response.data);
        })

      }
      else
      {
        await axios.get('/date/?&start=' +  this.state.startDate + '&end=' + this.state.endDate)
          .then(response => {
            commit("updateCalcRange", response.data);
        })
      }

      commit('changeLoading', false);
    }
  },

  mutations:
  {
    changeInitFinished(state, bool)
    {
      state.initFinished = bool;
    },
    
    setNoRange(state, bool)
    {
      state.noRange = bool;
    },
    
    changeLoading(state, bool)
    {
      state.loading = bool;
    },

    updateMonthly(state, value)
    {
      state.monthly = value.toExponential().substring(0, 4) + value.toExponential().substring(value.toExponential().length - 3, value.toExponential().length);
    },
    updateWeekly(state, value)
    {
      state.weekly = value[0].toExponential().substring(0, 4) + value[0].toExponential().substring(value[0].toExponential().length - 3, value[0].toExponential().length);
    },
    updateDaily(state, value)
    {
      state.daily = value[0].toExponential().substring(0, 4) + value[0].toExponential().substring(value[0].toExponential().length - 3, value[0].toExponential().length);
      state.pie_power[0] = value[1];
      state.pie_power[1] = value[2];
      state.pie_power[2] = value[3];
      state.pie_power[3] = value[4];
    },
    updateLineData(state, data)
    {
      state.max = data[0];
      state.min = data[0];
      for (var i = 0; i < data.length; i++)
      {
        if (data[i] > state.max)
        {
          state.max = data[i];
        }
        
        if (data[i] < state.min)
        {
          state.min = data[i];
        }
        state.line_data[i] = data[i].toExponential();
      }

    },
    updateLineDate(state, dates)
    {
      state.line_date = dates;
    },
    updatePiePower(state, data)
    {
      state.pie_power = data;
    },
    selectDate(state, data)
    {
      state.startDate = data["start"];
      state.endDate = data["end"];
    },
    updateCalcRange(state, value)
    {
      console.log(value);
      state.calcRange = value[0].toExponential().substring(0, 4) + value[0].toExponential().substring(value[0].toExponential().length - 3, value[0].toExponential().length);
      state.pie_power[0] = value[1];
      state.pie_power[1] = value[2];
      state.pie_power[2] = value[3];
      state.pie_power[3] = value[4];
    }
  }
});
