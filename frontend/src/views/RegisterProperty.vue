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
            @resultChanged="placeDetail => place = placeDetail"
            @resultCleared="() => place = null"
            @blur = "sendPlace"
          >
            <div slot="input" slot-scope="{ context, events, actions }">
              <label for="locationInput" class="block my-4 text-xl text-grey-dark">Address Search</label>
              <input
                v-model="context.input"
                @focus="events.inputHasReceivedFocus"
                @input="events.inputHasChanged"
                @keydown.enter.prevent="actions.selectItemFromList"
                @keydown.down.prevent="actions.shiftResultsSelection"
                @keydown.up.prevent="actions.unshiftResultsSelection"
                type="search"
                id="locationInput"
                class="p-4 w-full max-w-sm outline-none rounded-t-lg"
                placeholder="Type something ..."
                autocomplete="off"
              >
            </div>

            <span slot="item" slot-scope="{ place }" class="block p-2">
              {{ place.description }}
            </span>
            <span slot="activeItem" slot-scope="{ place }" class="block p-2 rounded bg-green-lightest font-bold">
              {{ place.description }}
            </span>
          </google-places-autocomplete>

          <!-- <h3 class="mt-8 text-grey-dark" v-if="place">Result</h3>
          <pre v-html="place" class="text-xs" /> -->
        </div>

      </el-col>
    </el-row>

    
  </div>
</template>


<script>
import Header from "@/components/Header.vue";
import { mapMutations } from "vuex";
import { GooglePlacesAutocomplete } from 'vue-better-google-places-autocomplete'

export default {
  components: {
    Header,
    GooglePlacesAutocomplete,
  },
  data() {
    return {
      place:null,
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
};
</script>

<style>
.vbga-results {
  @apply .list-reset w-full max-w-sm p-4 bg-white border-t rounded-b-lg;
}
</style>