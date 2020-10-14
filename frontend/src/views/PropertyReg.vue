<template>
  <div class="propreg">
    <Header>
      <template v-if="this.hasLogin">
        <el-dropdown trigger="click" @command="handleCommand">
          <div class="user">
            <el-avatar :size="50" :src="avatar"></el-avatar>
            <p>Welcome! {{ firstname }}</p>
          </div>
          <el-dropdown-menu slot="dropdown">
            <el-dropdown-item command="profile">My profile</el-dropdown-item>
            <el-dropdown-item command="auction">My Auctions</el-dropdown-item>
            <el-dropdown-item command="notification">Notifications</el-dropdown-item>
            <el-dropdown-item command="logout">Log out</el-dropdown-item>
          </el-dropdown-menu>
        </el-dropdown>
      </template>
      <template v-else>
        <el-button round @click="goto('login')">Sign In</el-button>
        <el-button round type="primary" @click="goto('register')">Sign Up</el-button>
      </template>
    </Header>

    <h1 class="title">Property Registration</h1>
    <div style="height: 100px; margin:0 15%;">
      <el-steps  :active="activateIndex - 0" align-center finish-status="success" >
        <el-step title="Basic"></el-step>
        <el-step title="Interior"></el-step>
        <el-step title="Keywords"></el-step>
        <el-step title="Photos"></el-step>
        <el-step title="Auction"></el-step>
      </el-steps>
    </div>


    <el-form :model="form"  label-width="80px" label-position="left">
      <el-tabs v-model="activateIndex" :tab-position="'left'" style="margin:0 20%" >
        <el-tab-pane label="Basic" name="0">
          <div>
            <google-places-autocomplete
                    @resultChanged="(placeDetail) => (place = placeDetail)"
                    @resultCleared="() => (place = null)"
            >
              <div slot="input" slot-scope="{ context, events, actions }">
<!--                <label for="locationInput" class="middle address-label">Address Search</label><br/>-->
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
                        placeholder="Please type in your property's address here for validation."
                        autocomplete="off"
                />
              </div>
              <span slot="item" slot-scope="{ place }" class="btn btn-default middle search-span">
                {{ place.description }}
            </span>
              <span
                      slot="activeItem"
                      slot-scope="{ place }"
                      class="btn btn-default middle search-span">
                {{ place.description }}
            </span>
            </google-places-autocomplete>
          </div>

          <el-row type="flex" justify="center">
            <el-col :span="20">
              <el-form
                      class="form"
                      ref="form"
                      :model="form"
                      :rules="rules"
                      label-width="150px"
                      label-position="left"
              >
                <el-row :gutter="50">
                  <el-col :span="24">
                    <el-form-item label="Address:" prop="address">
                      <el-input v-model="form.address"  ></el-input>
                    </el-form-item>
                    <el-form-item label="Suburb:" prop="suburb">
                      <el-input v-model="form.suburb" ></el-input>
                    </el-form-item>
                    <el-form-item label="State:" prop="state">
                      <el-input v-model="form.state" ></el-input>
                    </el-form-item>
                    <el-form-item label="Postcode:" prop="postcode">
                      <el-input v-model="form.postcode"></el-input>
                    </el-form-item>
                    <el-form-item label="Country:" prop="country">
                      <el-input v-model="form.country"></el-input>
                    </el-form-item>
                  </el-col>
                </el-row>
              </el-form>
            </el-col>
          </el-row>
        </el-tab-pane>

        <el-tab-pane label="Interior" name="1">
          <el-row type="flex" justify="center">
            <el-col :span="20">
              <el-form
                      class="form"
                      ref="form"
                      :model="form"
                      :rules="rules"
                      label-width="150px"
                      label-position="left"
              >
                <el-row :gutter="50">
                  <el-col :span="24">
                    <el-form-item label="Type:" v-model="form.type" prop="area">
                      <el-radio v-model="form.type" label="Apartment">Apartment</el-radio>
                      <el-radio v-model="form.type" label="Studio">Studio</el-radio>
                      <el-radio v-model="form.type" label="Unit">Unit</el-radio>
                      <el-radio v-model="form.type" label="House">House</el-radio>
                    </el-form-item>
                    <el-form-item label="Area:" prop="area">
                      <el-input v-model="form.area"></el-input>
                    </el-form-item>
                    <el-form-item label="Bathroom Number:" prop="bathNum">
                      <el-input v-model="form.bathNum"></el-input>
                    </el-form-item>
                    <el-form-item label="Bedroom Number:" prop="bedroomNum">
                      <el-input v-model="form.bedroomNum"></el-input>
                    </el-form-item>
                    <el-form-item label="Garage Number:" prop="carNum">
                      <el-input v-model="form.carNum"></el-input>
                    </el-form-item>
                  </el-col>
                </el-row>
              </el-form>
            </el-col>
          </el-row>

        </el-tab-pane>

        <el-tab-pane label="Keywords" name="2">
          <el-row type="flex" justify="space-between" style="margin:0 5%">
            <el-col :span="24">
              <el-form
                      class="form"
                      ref="form"
                      :model="form"
                      :rules="rules"
                      label-width="150px"
                      label-position="left"
              >
                <el-row >

            <el-checkbox-group v-model="form.keywords">
              <el-form-item label="Location:">
                  <el-col :span="8">
                  <el-checkbox label="Close To Schools"></el-checkbox>
                  <el-checkbox label="Close To Shops"></el-checkbox>
                  <el-checkbox label="Close To Bus Stop"></el-checkbox>
                  </el-col>
                <el-col :span="8">
                    <el-checkbox label="Close To Airport"></el-checkbox>
                      <el-checkbox label="Close To Library"></el-checkbox>
                      <el-checkbox label="Close To Park"></el-checkbox>
                </el-col>
                  <el-col :span="8">
                      <el-checkbox label="Close To Train Station"></el-checkbox>
                      <el-checkbox label="Close To Medical"></el-checkbox>
                  </el-col>
              </el-form-item>

              <el-form-item label="Details:">
                  <el-col :span="8">
                <el-checkbox label="BBQ"></el-checkbox>
                <el-checkbox label="Built-In Wardrobes"></el-checkbox>
                <el-checkbox label="Gym"></el-checkbox>
                <el-checkbox label="Swimming Pool"></el-checkbox>
                  </el-col>
                  <el-col :span="8">
                <el-checkbox label="Floor Covers"></el-checkbox>
                <el-checkbox label="Extractor Fan"></el-checkbox>
                <el-checkbox label="Drapes"></el-checkbox>
                <el-checkbox label="Dishwasher"></el-checkbox>
                  </el-col>
                  <el-col :span="8">
                      <el-checkbox label="Burglar Alarm"></el-checkbox>
                  </el-col>
              </el-form-item>

                <el-form-item label="Description:">
                    <el-input
                            placeholder="Enter the description for your property."
                            prefix-icon="el-icon-edit"
                            v-model="form.description"
                            type="textarea"
                            maxlength="200">
                    </el-input>
                </el-form-item>
            </el-checkbox-group>

                </el-row>
              </el-form>
            </el-col>
          </el-row>

        </el-tab-pane>

        <el-tab-pane label="Photos" name="3">
          <el-row type="flex" justify="center">
            <el-col :span="15" style="align-items: center">
            <label class="submit-label">Please upload your property photos here, no more than 5 detail photos.</label>
            </el-col>
          </el-row>
            <el-row type="flex" justify="center">
            <el-col :span="20">
              <el-form
                      class="form"
                      ref="form"
                      :model="form"
                      :rules="rules"
                      label-width="150px"
                      label-position="left"
              >
                <el-row :gutter="50">
                  <el-col :span="6">
                      <div class="user-avatar-container">
                          <div class="user-avatar">
                              <el-upload
                                      class="avatar-uploader"
                                      action="upload"
                                      accept="image/*"
                                      :auto-upload="false"
                                      :on-change="coverChange"
                                      :before-upload="beforeAvatarUpload"
                              >
                                  <img v-if="form.coverUrl" :src="form.coverUrl" class="avatar" />
                                  <i v-else class="el-icon-plus avatar-uploader-icon"></i>
                              </el-upload>
                          </div>
                          <div style="margin-top: 10px;">
                              <el-row type="flex" justify="space-around" style="align-items: center;">
                                  <label>Cover Photo</label>
                              </el-row>
                          </div>
                      </div>
                  </el-col>
                    <el-col :span="15">
                      <el-upload
                              multiple
                              class="avatar-uploader"
                              action="upload"
                              accept="image/*"
                              :auto-upload="false"
                              list-type="picture-card"
                              :limit="5"
                              :on-exceed="exceedTips"
                              :on-change="imgBroadcastChange"
                              :before-upload="beforeAvatarUpload"
                      >
                        <i class="el-icon-plus"></i>
                      </el-upload>
                        <div style="margin-top: 10px;">
                            <el-row type="flex" justify="space-around" style="align-items: center;">
                                <label>Detail Photos</label>
                            </el-row>
                        </div>
                    </el-col>
<!--                    </el-form-item>-->
                </el-row>
              </el-form>
            </el-col>
          </el-row>
        </el-tab-pane>

        <el-tab-pane label="Auction" name="4">
          <el-row type="flex" justify="center">
            <el-col :span="20">
              <el-form
                      class="form"
                      ref="form"
                      :model="form"
                      :rules="rules"
                      label-width="150px"
                      label-position="left"
              >
                <el-row :gutter="50">
                  <el-col :span="24">
                    <el-form-item label="Register for Auction:" prop="ifAuction">
                    <el-switch
                            v-model="form.isAuction"
                            active-color="#13ce66"
                            inactive-color="#ff4949">
                    </el-switch>
                    </el-form-item>
          <el-form-item v-if="form.isAuction" label="Time Range:" prop="daterange">
            <el-date-picker style="width:80%"
                    v-model="form.daterange"
                    type="daterange"
                    range-separator="To"
                    start-placeholder="Auction Start Time"
                    end-placeholder="Auction Start Time">
            </el-date-picker>
          </el-form-item>

          <el-form-item v-if="form.isAuction" label="Reserved Price:" prop="price">
            <el-input v-model="form.price"></el-input>
          </el-form-item>
                  </el-col>
                </el-row>
              </el-form>
            </el-col>
          </el-row>
        </el-tab-pane>

        <el-tab-pane label="Submit" name="5">
          <el-form :model="form"  label-width="80px" label-position="left">
          <el-row type="flex" justify="center">
          <label style="font-size:20px;margin: 80px 0;font-weight: bold;">Congratulations! You have finished a property registration.</label>
          </el-row>
            <el-row type="flex" justify="center">
            <el-button round type="primary" @click="submit">Submit</el-button>
          </el-row>
          </el-form>
        </el-tab-pane>
      </el-tabs>
    </el-form>


  </div>

</template>

<script>
import Header from "@/components/Header.vue";
import { GooglePlacesAutocomplete } from "vue-better-google-places-autocomplete";

import { mapActions } from "vuex";
export default {
  name: "PropertyRegistration",
  components: {
    Header,
    GooglePlacesAutocomplete,
  },
  data() {
    const checkInt = (rule, value, callback) => {
      const intReg = /^[0-9]+$/;
      if (!intReg.test(value)) {
        callback(new Error("Please input an integer"));
      } else {
        callback();
      }
    };
    return {
      place: null,
      inputDisable:true,
      dialogImageUrl: "",
      dialogVisible: false,
      hasLogin: false,
      activateIndex: '0',
      form: {
        country:'',
          description:'',
        isAuction:true,
        bathroomNum: '',
        bedroomNum: '',
        garageNum: '',
        address: "",
        suburb: "",
        state: "",
        postcode: "",
        area: '',
          coverUrl:'',
          coverRaw:'',
          imageUrl: [],
        imageRaw: [],
        daterange:[],
        keywords: [],
        type:'',
        // startDate: "",
        // endDate: "",
        daterange:'',
        price: "",
      },
      rules: {
        bedroomNum:[{ required: true, message: " Please enter bedroom number", trigger: "blur"},{validator:checkInt, trigger: "blur" },],
        carNum:[{ required: true, message: " Please enter garage number", trigger: "blur"},{validator:checkInt, trigger: "blur" },],
        bathNum:[{ required: true, message: " Please enter bathroom number", trigger: "blur"},{validator:checkInt, trigger: "blur" },],
        address: [{ required: true, message: " Please enter address", trigger: "blur" },],
        suburb: [{ required: true, message: " Please enter suburb", trigger: "blur" },],
          imageUrl: [{ required: true, message: " Please upload cover photo", trigger: "blur" },],
        state: [{ required: true, message: "Please enter state", trigger: "blur" },],
        postcode: [{required: true, message: " Please enter postcode", trigger: "blur",},],
        area: [{ required: true, message: " Please enter area", trigger: "blur"},{validator:checkInt, trigger: "blur" },],
        daterange: [{required: true, message: " Please enter start date", trigger: "blur",},],
        country: [{required: true, message: " Please enter end date", trigger: "blur",},],
        price: [{required: true, message: " Please enter price", trigger: "blur"}, {validator: checkInt,trigger: "blur" },],
      },
    };
  },
  created() {
    this.username = localStorage.getItem("username");
    // this.username = this.$store.state.username;
    if (this.username !== null) {
      this.hasLogin = true;
      this.avatar = localStorage.getItem("avatar");
    }
    this.firstname = localStorage.getItem("firstname");
  },
  methods: {
    ...mapActions(["logout"]),
    handleCommand(command) {
      switch (command) {
        case "profile":
          this.$router.push("/profile");
          break;
        case "auction":
          this.$router.push("/auction");
          break;
        case "notification":
          break;
        case "logout":
          this.$axios.post('/user/logout')
                  .then((response) => {
                    if (response.status >= 200 && response.status < 300){
                      if (response.data.code === 200){
                        this.logout();
                        location.reload()
                      }else{
                        console.log(response.msg)
                      }
                    }else{
                      console.log(response.msg)
                    }
                  })
          break;
        default:
          break;
      }
    },
    beforeAvatarUpload(file) {
      const isLt2M = file.size / 1024 / 1024 < 2;
      let types = ["image/jpeg", "image/jpg", "image/png"];
      const isImage = types.includes(file.type);
      if (!isImage) {
        this.$message.error("上传图片只能是 JPG、JPEG、PNG 格式!");
      }
      if (!isLt2M) {
        this.$message.error("Image size can not larger than 2MB!");
      }
      return isImage && isLt2M;
    },
      coverChange(file){
          this.form.imageRaw = file.raw;
          this.form.imageUrl = URL.createObjectURL(file.raw);
      },
    imgBroadcastChange(file) {
      this.form.imageRaw.push(file.raw);
      this.form.imageUrl.push(URL.createObjectURL(file.raw));
    },
    exceedTips: function () {
      this.$message.error("Maximum 5 photos.");
    },

    submit() {
      this.$refs["form"].validate((valid) => {
        if (valid) {
          let data = new FormData();
          data.append('bathroomNum', this.form.bathNum);
          data.append('bedroomNum', this.form.bedroomNum);
          data.append('garageNum', this.form.carNum);
          data.append('type', this.form.type);
          data.append('address', this.form.address);
          data.append('suburb', this.form.suburb);
          data.append('state', this.form.state);
          data.append('postcode', this.form.postcode);
          data.append('area', this.form.area);
          data.append('country', this.form.country);
          data.append('daterange', this.form.daterange);
          data.append('price', this.form.price);
          data.append('keywords', this.form.keywords);
          data.append('cover', this.form.coverRaw);
          data.append('isAuction', this.form.isAuction);
          data.append('description', this.form.description);

          this.form.imageRaw.forEach(function (file) {
                    data.append('photos', file, file.name);
                 });

          this.$axios.post('/property/registration', data)
                  .then((response) => {
                    if (response.status >= 200 && response.status < 300) {
                      if(response.data.code === 200){
                        console.log(response.data);
                        this.$message.success("Property register successful");
                        this.$router.replace("/auction");
                      }
                    } else if(response.data.code === 400){
                      console.log(response.data);
                      this.$message.success("Property register failure");
                      location.reload()
                    }else{
                      console.log(response.msg);
                    }
                  })
                  .catch((res) => {
            console.log('error', res);
            this.$message.error('Property Register Error');
          });
        } else {
          return false;
        }
      });
    },
    goto(name) {
      console.log(name);
      this.$router.push({ name: name });
    },
    back() {
      this.$router.go(-1);
    },
  },
  watch:{
    place: function(newPlace,oldPlace){
      if(this.place==null){
        this.$set(this.form,'address',"");
        this.$set(this.form,'postcode',"");
        this.$set(this.form,'suburb',"");
        this.$set(this.form,'state',"");
        this.inputDisable = true;
        return ;
      }

      this.inputDisable = false;

      let postcode, street,suburb, state;
      let place_info = this.place.formatted_address.split(",");

      let country = place_info[place_info.length-1].trim();

      if(country!="Australia"){
        this.$message.error("Propery for registration must locate in Australia!!");
        setTimeout(function(){
          // location.reload();
        },2000);
        return;
      }

      if (place_info.length<3){
        this.$message.error("On Google Map, this is not a residential area!");
        setTimeout(function(){
          // location.reload();
        },2000);
        return;
      }

      let mid_info = place_info[place_info.length-2].split(" ");
      let suburb_array = mid_info.slice(0,mid_info.length-2);
      suburb = suburb_array.join(" ");
      postcode = mid_info[mid_info.length-1];
      state = mid_info[mid_info.length-2];
      street = place_info.slice(0,place_info.length-2).join(" ");
      this.$set(this.form,'address',street.trim());
      this.$set(this.form,'postcode',postcode.trim());
      this.$set(this.form,'suburb',suburb.trim());
      this.$set(this.form,'state',state.trim());
      this.$set(this.form,'country',country.trim());
      return;
    }
  },
};
</script>

<style scoped lang="scss">
.user {
  display: flex;
  align-items: center;
  p {
    margin-left: 10px;
  }
}
.title {
  margin: 30px;
  text-align: center;
}
.form {
  padding: 30px;
  border-radius: 15px;
}
.user-avatar{
    /*margin-top: 20px;*/
    text-align: center;
}
.avatar-uploader .el-upload {
  cursor: pointer;
  position: relative;
  overflow: hidden;
}
.avatar-uploader .el-upload:hover {
  border-color: #409eff;
}
.avatar-uploader-icon {
  border: 1px dashed #d9d9d9 !important;
  border-radius: 6px;
  font-size: 28px;
  color: #8c939d;
  width: 150px;
  height: 150px;
  line-height: 150px;
  text-align: center;
}
.avatar {
  margin: 5px;
  width: 150px;
  height: 150px;
  display: block;

}
.number-criteria {
  .el-col {
    margin: 0 10px;
    display: flex;
    align-items: center;
  }
  img {
    width: 24px;
    height: 16px;
    margin-right: 10px;
  }
}
.keywords-criteria2 {
  .el-col {
    margin: 0 10px;
    display: flex;
    align-items: center;
  }
  img {
    width: 24px;
    height: 16px;
    margin-right: 10px;
  }
}
.el-checkbox {
  display: block;
}

.address-label {
  margin-bottom: 15px;
  width: 180px;
  text-align: center;
  background-color: rgba(19, 50, 100, 0.8);
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
  background-color: rgba(69, 95, 147, 0.8);
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

.submit-label{
  font-size: 18px;
  margin:15px 0;
}

.address-info{
  margin-top:10px;
  margin-bottom:0px;
  padding-bottom:0px;
}

#locationInput{
    width: 80%;
}

.search-span{
    width:78%;
    background-color: rgba(200, 213, 249, 0.4);
}

span {
  border: 1px solid rgba(138, 138, 138, 0.4);
}


.el-checkbox {
  display: block;
}

</style>
