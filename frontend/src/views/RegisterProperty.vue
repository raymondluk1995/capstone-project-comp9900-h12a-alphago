<template>
  <div class="register">
    <Header>
      <el-button round @click="goto('login')">Sign In</el-button>
      <el-button round type="primary" @click="back">Back</el-button>
    </Header>

    <el-row type="flex" justify="center">
      <el-col :span="14">
        <h1 class="title">Property Registration</h1>
        <div>
          <google-places-autocomplete
            @resultChanged="(placeDetail) => (place = placeDetail)"
            @resultCleared="() => (place = null)"
            @blur="sendPlace"
          >
            <div slot="input" slot-scope="{ context, events, actions }">
              <label for="locationInput" class="middle address-label"
                >Address Search</label
              ><br />
              <input
                v-model="context.input"
                @focus="events.inputHasReceivedFocus"
                @input="events.inputHasChanged"
                @keydown.enter.prevent="actions.selectItemFromList"
                @keydown.down.prevent="actions.shiftResultsSelection"
                @keydown.up.prevent="actions.unshiftResultsSelection"
                type="search"
                id="locationInput"
                class="middle form-control"
                placeholder="Please type in your property's address for validation"
                autocomplete="off"
              />
            </div>

            <span slot="item" slot-scope="{ place }" class="btn btn-default">
              {{ place.description }}
            </span>
            <span
              slot="activeItem"
              slot-scope="{ place }"
              class="btn btn-default"
            >
              {{ place.description }}
            </span>
          </google-places-autocomplete>

          <p v-if="place" id="p1">
            The auto generated address is given below. You can edit them in the
            input box.
          </p>
          <!-- <pre v-if="place" v-html="place.formatted_address" class="pre-place"/> -->

          <div v-if="place">
            
            <div>
              <p class="address-info">Address Line</p>
              <input class="form-control" :value="steet_placeholder"/>
            </div>


            <div>
              <p class="address-info">Postcode</p>
              <input class="form-control" :value="postcode_placeholder"/>
            </div> 

            <div>
              <p class="address-info">City Or Suburb</p>
              <input class="form-control" :value="suburb_placeholder"/>
            </div> 

            <div>
              <p class="address-info">State</p>
              <input class="form-control" :value="state_placeholder"/>
            </div> 

          </div>
        </div>
      </el-col>
    </el-row>
  </div>
</template>


<script>
import Header from "@/components/Header.vue";
import { mapMutations } from "vuex";
import { GooglePlacesAutocomplete } from "vue-better-google-places-autocomplete";

export default {
  components: {
    Header,
    GooglePlacesAutocomplete,
  },
  data() {
    return {
      place: null,

    };
  },
  methods: {
    submitForm(formName) {
      this.$refs[formName].validate((valid) => {
        if (valid) {
          alert("submit!");
        } else {
          console.log("error submit!!");
          return false;
        }
      });
    },
    resetForm(formName) {
      this.$refs[formName].resetFields();
    },
  },
  computed:{
    postcode_placeholder(){
      if(this.place==null){
        return null;
      }
      return (this.place.address_components[6].long_name);
    },

    steet_placeholder(){
      if(this.place==null){
        return null;
      }
      return (this.place.address_components[0].long_name+" "+this.place.address_components[1].long_name);
    },

    suburb_placeholder(){
      if(this.place==null){
        return null;
      }
      return (this.place.address_components[2].long_name);
    },

    state_placeholder(){
      if(this.place==null){
        return null;
      }
      return (this.place.address_components[4].long_name);
    },
  }
};
</script>

<style>
.address-label {
  width: 180px;
  text-align: center;
  background-color: rgba(15, 128, 255, 0.8);
  border-radius: 5px;
  font-size: 20px;
  color: white;
}

.middle {
  position: relative;
  display: inline-block;
  left: 50%;
  -webkit-transform: translate(-50%, 0);
  transform: translate(-50%, 0);
}

li {
  list-style-type: none;
}

.btn-default:hover {
  background-color: rgba(15, 128, 255, 0.8);
}

.pre-place {
  font-weight: bold;
  border-radius: 5px;
  font-size: 20px;
}

#p1 {
  margin-top: 10px;
  font-size: 20px;
}

.address-info{
  margin-top:10px;
  margin-bottom:0px;
  padding-bottom:0px;
}
</style>