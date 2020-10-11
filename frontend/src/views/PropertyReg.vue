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
            <el-dropdown-item command="notification"
              >Notifications</el-dropdown-item
            >
            <el-dropdown-item command="logout">Log out</el-dropdown-item>
          </el-dropdown-menu>
        </el-dropdown>
      </template>
      <template v-else>
        <el-button round @click="goto('login')">Sign In</el-button>
        <el-button round type="primary" @click="goto('register')"
          >Sign Up</el-button
        >
      </template>
    </Header>

    <h1 class="title">Property Registration</h1>

    <div>
          <google-places-autocomplete
            @resultChanged="(placeDetail) => (place = placeDetail)"
            @resultCleared="() => (place = null)"
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
                placeholder="Please type in your property's address here"
                autocomplete="off"
              />
            </div>

           
            <span slot="item" slot-scope="{ place }" class="btn btn-default middle search-span">
                {{ place.description }}
            </span>
            <span
                slot="activeItem"
                slot-scope="{ place }"
                class="btn btn-default middle search-span"
            >
                {{ place.description }}
            </span>


            
          </google-places-autocomplete>         
    </div>

    <el-row type="flex" justify="center">
      <el-col :span="12">
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
                <el-input v-model="form.address" :disabled="inputDisable" ></el-input>
              </el-form-item>
              <el-form-item label="Suburb:" prop="suburb">
                <el-input v-model="form.suburb" :disabled="inputDisable"></el-input>
              </el-form-item>
              <el-form-item label="State:" prop="state">
                <el-input v-model="form.state" :disabled="inputDisable"></el-input>
              </el-form-item>
              <el-form-item label="Postcode:" prop="postcode">
                <el-input v-model="form.postcode" :disabled="inputDisable"></el-input>
              </el-form-item>
              <el-form-item label="Area:" prop="area">
                <el-input v-model="form.area"></el-input>
              </el-form-item>

              <el-row type="flex" justify="space-between">
                <el-col>
                  <el-row
                    type="flex"
                    justify="space-between"
                    class="search-criteria"
                  >
                    <el-col>
                      <img src="@/assets/bath.png" alt="" />
                      <el-select v-model="bathNum">
                        <el-option
                          v-for="item in nums"
                          :key="item.value"
                          :label="item.label"
                          :value="item.value"
                        >
                        </el-option>
                      </el-select>
                    </el-col>
                    <el-col>
                      <img src="@/assets/bed.png" alt="" />
                      <el-select v-model="bedroomNum">
                        <el-option
                          v-for="item in nums"
                          :key="item.value"
                          :label="item.label"
                          :value="item.value"
                        >
                        </el-option>
                      </el-select>
                    </el-col>
                    <el-col>
                      <img src="@/assets/parking.png" alt="" />
                      <el-select v-model="carNum">
                        <el-option
                          v-for="item in nums"
                          :key="item.value"
                          :label="item.label"
                          :value="item.value"
                        >
                        </el-option>
                      </el-select>
                    </el-col>
                  </el-row>
                </el-col>
              </el-row>

              <el-form-item label="Keywords:">
                <el-checkbox-group v-model="form.keywords">
                  <el-row
                    type="flex"
                    justify="space-between"
                    class="search-criteria"
                  >
                    <el-col>
                      <el-checkbox label="These"></el-checkbox>
                      <el-checkbox label="are"></el-checkbox>
                      <el-checkbox label="some"></el-checkbox>
                      <el-checkbox label="keywords"></el-checkbox>
                    </el-col>
                  </el-row>
                </el-checkbox-group>
              </el-form-item>
              <el-form-item label="Start Date:" prop="startDate">
                <el-date-picker v-model="form.startDate" type="date">
                </el-date-picker>
              </el-form-item>
              <el-form-item label="End Date:" prop="endDate">
                <el-date-picker v-model="form.endDate" type="date">
                </el-date-picker>
              </el-form-item>
              <el-form-item label="Reserved Price:" prop="price">
                <el-input v-model="form.price"></el-input>
              </el-form-item>
              <el-form-item label="Photos:" prop="photo">
                <el-upload
                  :multiple="true"
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
              </el-form-item>

              <div class="btns">
                <el-button round type="primary" @click="submit"
                  >Submit</el-button
                >
              </div>
            </el-col>
          </el-row>
        </el-form>
      </el-col>
    </el-row>
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
    var checkInt = (rule, value, callback) => {
      if (!value) {
        return callback(new Error("This cannot be empty"));
      }
      setTimeout(() => {
        if (!Number.isInteger(value)) {
          callback(new Error("Please input an integer"));
        } else {
          callback();
        }
      }, 1000);
    };
    return {
      place: null,
      inputDisable:true,
      dialogImageUrl: "",
      dialogVisible: false,
      // hasLogin: false,
      hasLogin: true,
      bathNum: 1,
      bedroomNum: 1,
      carNum: 1,
      avatar:null,
      nums: [
        {
          value: 1,
          label: 1,
        },
        {
          value: 2,
          label: 2,
        },
        {
          value: 3,
          label: 3,
        },
        {
          value: 4,
          label: 4,
        },
        {
          value: 5,
          label: 5,
        },
      ],
      form: {
        address: "",
        suburb: "",
        state: "",
        postcode: "",
        area: "",
        imageUrl: [],
        imageRaw: [],
        keywords: [],
        startDate: "",
        endDate: "",
        price: "",
      },
      rules: {
        address: [
          { required: true, message: " Please enter address", trigger: "blur" },
        ],
        suburb: [
          { required: true, message: " Please enter suburb", trigger: "blur" },
        ],
        state: [
          { required: true, message: "Please enter state", trigger: "blur" },
        ],
        postcode: [
          {
            required: true,
            message: " Please enter postcode",
            trigger: "blur",
          },
        ],
        area: [
          { required: true, message: " Please enter area", trigger: "blur",validator:checkInt},
        ],
        startDate: [
          {
            required: true,
            message: " Please enter start date",
            trigger: "blur",
          },
        ],
        endDate: [
          {
            required: true,
            message: " Please enter end date",
            trigger: "blur",
          },
        ],
        price: [
          {
            required: true,
            message: " Please enter price",
            trigger: "blur",
            validator: checkInt,
          },
        ],
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
          // this.$router.push("/auction");
          break;
        case "notification":
          break;
        case "logout":
          this.$axios.post("/user/logout").then((response) => {
            if (response.status >= 200 && response.status < 300) {
              if (response.data.code === 200) {
                this.logout();
                location.reload();
              } else {
                console.log(response.msg);
              }
            } else {
              console.log(response.msg);
            }
          });
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
    imgBroadcastChange(file) {
      this.form.imageRaw.push(file.raw);
      this.form.imageUrl.push(URL.createObjectURL(file.raw));
    },
    exceedTips: function () {
      this.$message.error("最多只能上传五张图片");
    },

    submit() {
      this.$refs["form"].validate((valid) => {
        if (valid) {
          this.$message.success("Submit successful");
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
    goto(name) {
      console.log(name);
      this.$router.push({ name: name });
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
          location.reload();
        },2000);
        return;
      }
      
      if (place_info.length<3){
        this.$message.error("On Google Map, this is not a residential area!");
        setTimeout(function(){
          location.reload();
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
.btns {
  margin-top: 50px;
  text-align: right;
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
  width: 120px;
  height: 120px;
  line-height: 120px;
  text-align: center;
}
.avatar {
  margin: 5px;
  width: 120px;
  height: 120px;
  display: block;
}
.search-criteria {
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

#locationInput{
    width: 1000px;
}

.search-span{
    width:1000px;
    background-color:rgba(102, 255, 102,0.4);
}

span {
  border: 1px solid rgba(0,0,0,0.4);
}

</style>
