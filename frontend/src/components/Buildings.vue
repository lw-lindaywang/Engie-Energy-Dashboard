<style>
  #spacer {
    padding-left: 4em;
  }

  .item {
    padding-left: 2em;
  }

  .top {
    padding-top: 5em;
  }

</style>

<template>
  <v-container fluid grid-list-xl>
    <div v-if="loading" class="top">
      <Loading/>
    </div>
    <div v-else-if="initFinished" class="top">
      <v-layout row justify-left wrap>
        <v-flex xs4 offset-xs1>
            <v-app>
                <v-tabs
                  centered
                  v-model="activeTab"
                  grow
                  slider-color="black"
                  color = "blue"
                >
                  <v-tab href="#tab-1" class="white--text">
                    Test Driver
                  </v-tab>
                  <v-tab  href="#tab-2" class="white--text">
                    Build Number
                  </v-tab>
                  <v-tab  href="#tab-3" class="white--text">
                    Test Case
                  </v-tab>
                  
                  <v-tabs-items>
                    <v-tab-item
                      v-for="k in 3"
                      :key="k"
                      :value="'tab-' + k"
                    >
                    </v-tab-item>
                  </v-tabs-items>
                </v-tabs>
                <div v-if="!isEmpty">
                  <v-expansion-panel v-model="panelIndexTest" v-if="activeTab == 'tab-1'" focusable>
                    <v-expansion-panel-content
                      v-for="node in tests"
                      v-bind:key="node"
                    >
                      <template v-slot:header>
                        <div>{{node}}</div>
                      </template>
                        <v-card>
                          <v-list>
                          <v-list-tile
                            v-for="all in panelContent('test')"
                            :key="all.key"
                            @click="select(all)"
                          >
                            <v-list-tile-content>
                              <v-list-tile-title class="item">
                                <v-tooltip right>
                                  <template v-slot:activator="{ on }">
                                    <span v-on="on">{{all.simplekey}}</span>
                                  </template>
                                  <span>{{all.simplekey}}</span>
                                </v-tooltip>
                              </v-list-tile-title>
                            </v-list-tile-content>
                          </v-list-tile>
                        </v-list>  
                      </v-card> 
                    </v-expansion-panel-content>
                  </v-expansion-panel>

                  <v-expansion-panel v-model="panelIndexBuild" v-else-if="activeTab == 'tab-2'" focusable>
                    <v-expansion-panel-content
                      v-for="node in builds"
                      v-bind:key="node"
                    >       
                      <template v-slot:header>
                        <div>{{node}}</div>
                      </template>
                      <v-card>
                        <v-list>
                            <v-list-tile
                              v-for="all in panelContent('build')"
                              :key="all.key"
                              @click="select(all)"
                            >
                              <v-list-tile-content>
                                  <v-list-tile-title class="item">
                                    <v-tooltip right>
                                      <template v-slot:activator="{ on }">
                                        <span v-on="on">{{all.simplekey}}</span>
                                      </template>
                                      <span>{{all.simplekey}}</span>
                                    </v-tooltip>
                                  </v-list-tile-title>
                              </v-list-tile-content>
                            </v-list-tile>
                        </v-list>
                      </v-card>
                    </v-expansion-panel-content>
                  </v-expansion-panel>

                  <v-expansion-panel v-model="panelIndexTestCase" v-else-if="activeTab == 'tab-3' && !noDuplicates" focusable>
                    <v-expansion-panel-content
                      v-for="node in testCases"
                      v-bind:key="node"
                    >
                      <template v-slot:header>
                        <div>{{node}}</div>
                      </template>
                      <v-card>
                        <v-list>
                            <v-list-tile
                              v-for="all in panelContent('testCase')"
                              :key="all.key"
                              @click="select(all)"
                            >
                              <v-list-tile-content>
                                <v-list-tile-title class="item">
                                  <v-tooltip right>
                                    <template v-slot:activator="{ on }">
                                      <span v-on="on">{{all.testCaseKey}}</span>
                                    </template>
                                    <span>{{all.testCaseKey}}</span>
                                  </v-tooltip>
                                </v-list-tile-title>
                              </v-list-tile-content>
                            </v-list-tile>
                        </v-list>
                      </v-card>
                    </v-expansion-panel-content>
                  </v-expansion-panel>
                  <v-expansion-panel v-model="panelIndexTestCase" v-else-if="activeTab == 'tab-3' && noDuplicates" focusable>
                    <v-expansion-panel>
                      <v-card-text>
                        No duplicate test cases found!
                      </v-card-text>
                    </v-expansion-panel>
                  </v-expansion-panel>
                </div>
                <div v-else>
                  <v-card>
                    <v-card-text>
                      No test cases found!
                    </v-card-text>
                  </v-card>
                </div>
            </v-app>
        </v-flex>
        <v-flex md6 id="spacer">
          <div v-if="selected">
            <v-card>
              <v-card-title>
                <h4>Test Case Information</h4>
              </v-card-title>
              <v-card-text>
                <a :href="selected.url" style="font-size: 30px" target="_blank">Link to Test Results</a>
                <br /><br />
                <div> <b>Simple Key:</b> {{selected.simplekey}} </div>
                <div> <b>Key:</b> {{selected.key}} </div>
                <br />
                <div> <b>Etag:</b> {{selected.etag}} </div>
                <div> <b>Storage Class:</b> {{selected.storageClass}} </div>
                <div> <b>Size:</b> {{selected.size}} </div>
              </v-card-text>
            </v-card>
          </div>
        </v-flex>
      </v-layout> 
    </div>
    <div v-else class="top">
      <v-container grid-list-md class="text-xs-center">
        <v-card>
          <br /><br />
          <v-card-text style="font-size: 25px">
            Please select a project from the dropdown!
          </v-card-text>
          <br /><br />
        </v-card>   
      </v-container>
    </div>
  </v-container>
</template>

<script>
import Loading from "./Loading.vue";
export default {

  components: {
    Loading
  },

  // methods to change selected data & to get panel content
  methods: {
    select: function(item) {
      this.$store.commit('updateSelectedData', item);
    },
    panelContent(category)
    {
      return this.$store.state.panelContent[category];
    }
  },


  // getter functions (from store)
  computed: {
    loading()
    {
      return this.$store.state.loading;
    },
    noDuplicates()
    {
      return this.$store.state.noDuplicates;
    },
    error() {
      return this.$store.state.error && this.$store.state.initFinished;
    },
    initFinished() {
      return this.$store.state.initFinished && this.$store.state.selectedProject.length > 0;
    },
    tests()
    {
      return this.$store.state.categories["test"];
    },
    builds()
    {
      return this.$store.state.categories["build"];
    },
    testCases()
    {
      return this.$store.state.categories["testCase"];
    },
    isEmpty()
    {
       return (this.$store.state.items.length === 0 && this.$store.state.initFinished && !this.$store.state.error && this.$store.state.selectedProject.length > 0);
    },
    panelIndexBuild:
    {
      get ()
      {
        return this.$store.state.panelIndex["build"];
      },
      set (value)
      {
        this.$store.commit('updatePanelIndex', {"category": "build", "index": value});
      }
    },
    panelIndexTest:
    {
      get ()
      {
        return this.$store.state.panelIndex["test"];
      },
      set (value)
      {
        this.$store.commit('updatePanelIndex', {"category": "test", "index": value});
      }
    },
    panelIndexTestCase:
    {
      get ()
      {
        return this.$store.state.panelIndex["testCase"];
      },
      set (value)
      {
        this.$store.commit('updatePanelIndex', {"category": "testCase", "index": value});
      }
    },
    activeTab:
    {
      get ()
      {
        return this.$store.state.activeTab;
      },
      set (value)
      {
        this.$store.commit('updateActiveTab', value)
      }
    },
    selected:
    {
      get ()
      {
        return this.$store.state.selectedData;
      },
      set (item)
      {
        this.$store.commit('updateSelectedData', item)
      }
    }
  }

};
</script>