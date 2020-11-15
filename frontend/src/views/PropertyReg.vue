<template>
  <div class="propreg">
    <Header>
        <template v-if="this.hasLogin">
            <el-dropdown trigger="hover" @command="handleCommand" style="align-items: center" placement="bottom">
                <div class="user" >
                    <el-badge v-if="parseInt(this.unread) !== 0" :value="this.unread" :max="99" class="item">
                        <el-avatar :size="70" :src="avatar"></el-avatar>
                    </el-badge>
                    <el-avatar  v-else :size="70" :src="avatar"></el-avatar>

                </div>
                <el-dropdown-menu slot="dropdown">
                    <el-dropdown-item command="profile" icon="el-icon-user-solid"> My Profile</el-dropdown-item>
                    <el-dropdown-item command="property"  icon="el-icon-house"> My Properties</el-dropdown-item>
                    <el-dropdown-item command="auction" icon="el-icon-s-home"> My Auctions</el-dropdown-item>

                    <el-dropdown-item command="notification"  icon="el-icon-bell">
                        Notifications <el-badge v-show="parseInt(this.unread) !== 0" class="mark" :value="this.unread" style="padding:0;background-color: white"/>
                    </el-dropdown-item>

                    <el-dropdown-item command="logout" icon="el-icon-turn-off"> Log out</el-dropdown-item>
                </el-dropdown-menu>
            </el-dropdown>
        </template>

      <template v-else>
          <div class="back-btn">
              <span  id="back-btn" style="padding:2px 5px;font-size:20px;" @click="goto('login')">Sign In <i class="el-icon-check"></i></span>
              <div class="bottom-line"></div>
          </div>
          <div class="back-btn">
              <span  id="back-btn2" style="padding:2px 5px;font-size:20px;" @click="goto('register')">Sign Up <i class="el-icon-user"></i></span>
              <div class="bottom-line"></div>
          </div>
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


    <el-form  label-width="80px" label-position="left">
      <el-tabs v-model="activateIndex" :tab-position="'left'" style="margin:0 20%" >
        <el-tab-pane label="Basic" name="0" >
          <div class="google-map">
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
                      ref="form1"
                      :model="form1"
                      :rules="rules"
                      label-width="150px"
                      label-position="left"
              >
                <el-row :gutter="50">
                  <el-col :span="24">
                    <el-form-item label="Address:" prop="address">
                      <el-input v-model="form1.address"  ></el-input>
                    </el-form-item>
                    <el-form-item label="Suburb:" prop="suburb">
                      <el-input v-model="form1.suburb" ></el-input>
                    </el-form-item>
                    <el-form-item label="State:" prop="state">
                      <el-input v-model="form1.state" ></el-input>
                    </el-form-item>
                    <el-form-item label="Postcode:" prop="postcode">
                      <el-input v-model="form1.postcode"></el-input>
                    </el-form-item>
                    <el-form-item label="Country:" prop="country">
                      <el-input v-model="form1.country"></el-input>
                    </el-form-item>
<!--                      <el-button type="primary" icon="el-icon-edit" circle ></el-button>-->
                  </el-col>
                </el-row>
              </el-form>
            </el-col>
          </el-row>
            <div class="next-btn">
            <el-button type="success" icon="el-icon-right" round @click="checktable1">Next</el-button>
            </div>
        </el-tab-pane>

        <el-tab-pane label="Interior" name="1" :disabled="dis1">
          <el-row type="flex" justify="center">
            <el-col :span="23">
              <el-form
                      class="form"
                      ref="form2"
                      :model="form2"
                      :rules="rules"
                      label-width="150px"
                      label-position="left"
              >
                <el-row :gutter="50">
                  <el-col :span="20">
                    <el-form-item label="Type:" prop="type">
                        <el-select v-model="form2.type" placeholder="Property Type" value="">
                            <el-option
                                    v-for="item in options"
                                    :key="item.value"
                                    :label="item.label"
                                    :value="item.value">
                            </el-option>
                        </el-select>
                    </el-form-item>
                    <el-form-item label="Area:" prop="area" >
                      <el-input v-model="form2.area"  maxlength="5" style="width:50%">
                          <i slot="suffix" class="input-slot">㎡</i>
                      </el-input>
<!--                        <template>-->
<!--                        <slot slot="suffix" class="input-slot">0/25</slot>-->
<!--                        </template>-->

                    </el-form-item>
                      <el-form-item label="Rooms:" prop="room">
                          <el-row type="flex" justify="center">
                          <el-col :span="24">
                              <el-col :span="8">
                                  <el-input
                                          placeholder="Bathroom Num"
                                          suffix-icon="el-icon-toilet-paper"
                                          maxlength="2"
                                          v-model="form2.bathroomNum">
                                  </el-input>
                              </el-col>
                              <el-col :span="8">
                                  <el-input
                                          placeholder="Bedroom Num"
                                          suffix-icon="el-icon-house"
                                          maxlength="2"
                                          v-model="form2.bedroomNum">
                                  </el-input>
                              </el-col>
                              <el-col :span="8">
                                  <el-input
                                          placeholder="Garage Num"
                                          suffix-icon="el-icon-truck"
                                          maxlength="2"
                                          v-model="form2.garageNum">
                                  </el-input>
                              </el-col>
                          </el-col>
                </el-row>
                      </el-form-item>
                  </el-col>
                </el-row>
              </el-form>
            </el-col>
          </el-row>
            <el-row type="flex" justify="center">
            </el-row>
            <div class="next-btn">
            <el-button type="success" icon="el-icon-right" round @click="checktable2">Next</el-button>
            </div>
        </el-tab-pane>

        <el-tab-pane label="Keywords"  :disabled="dis2" name="2">
          <el-row type="flex" justify="space-between" style="margin:0 5%">
            <el-col :span="24">
              <el-form
                      class="form"
                      ref="form3"
                      :model="form3"
                      :rules="rules"
                      label-width="150px"
                      label-position="left"
              >
                <el-row >

            <el-checkbox-group v-model="form3.kw_position">
              <el-form-item label="Location:" >
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
            </el-checkbox-group>

                    <el-checkbox-group v-model="form3.kw_details">
              <el-form-item label="Details:" >
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
                    </el-checkbox-group>

                <el-form-item label="Description:">
                    <el-input
                            placeholder="Enter the description for your property. No more than 1000 words."
                            prefix-icon="el-icon-edit"
                            v-model="form3.description"
                            type="textarea"
                            :rows="5"
                            maxlength="1000">
                    </el-input>
                </el-form-item>


                </el-row>
              </el-form>
            </el-col>
          </el-row>
            <div class="next-btn">
                <el-button type="success" icon="el-icon-right" round @click="checktable3">Next</el-button>
            </div>
        </el-tab-pane>

        <el-tab-pane label="Photos" :disabled="dis3" name="3">
          <el-row type="flex" justify="center">
            <el-col :span="15" style="align-items: center">
            <label class="submit-label">Please upload your property photos here, no more than 5 photos.</label>
            </el-col>
          </el-row>
            <el-row type="flex" justify="center">
            <el-col :span="20">
              <el-form
                      class="form"
                      ref="form4"
                      :model="form4"
                      :rules="rules"
                      label-width="150px"
                      label-position="left"
                      prop="photos"
              >
                <el-row :gutter="50">
                    <el-col :span="24">
                      <el-upload
                              multiple
                              class="avatar-uploader"
                              action="upload"
                              accept="image/*"
                              :auto-upload="false"
                              list-type="picture-card"
                              :limit="10"
                              :on-remove="handleRemove"
                              :on-exceed="exceedTips"
                              :on-change="imgBroadcastChange"
                              :before-upload="beforeAvatarUpload"
                      >
                        <i class="el-icon-plus"></i>
                      </el-upload>
                        <div style="margin-top: 10px;">
                        </div>
                    </el-col>
                </el-row>
              </el-form>
            </el-col>
          </el-row>
            <div class="next-btn">
                <el-button type="success" icon="el-icon-right" round @click="checktable4">Next</el-button>
            </div>
        </el-tab-pane>

        <el-tab-pane label="Auction" :disabled="dis4" name="4">
          <el-row type="flex" justify="center">
            <el-col :span="20">
              <el-form
                      class="form"
                      ref="form5"
                      :model="form5"
                      :rules="rules"
                      label-width="150px"
                      label-position="left"
              >
                <el-row :gutter="50">
                  <el-col :span="24">
                    <el-form-item label="Register for Auction:" prop="ifAuction">
                    <el-switch
                            v-model="form5.Auction"
                            active-color="#13ce66"
                            inactive-color="#ff4949">
                    </el-switch>
                    </el-form-item>
          <el-form-item v-if="form5.Auction" label="Time Range:" prop="daterange">
            <el-date-picker style="width:80%"
                    v-model="form5.daterange"
                    type="datetimerange"
                    range-separator="To"
                    start-placeholder="Auction Start Time"
                    end-placeholder="Auction End Time"
                    value-format="timestamp"
                    :picker-options="pickerOptions">
            </el-date-picker>

          </el-form-item>

          <el-form-item v-if="form5.Auction" label="Reserved Price:" prop="price">
            <el-input v-model="form5.price" maxlength="10" >
                <i slot="suffix" class="input-slot">{{form5.price |numFormat}} A$</i>
            </el-input>
          </el-form-item>

          <el-form-item v-if="form5.Auction" label="Starting Price:" prop="minimumPrice">
              <el-input v-model="form5.minimumPrice"  maxlength="10" >
                  <i slot="suffix" class="input-slot">{{form5.minimumPrice|numFormat}} A$</i>
              </el-input>
          </el-form-item>


                  </el-col>
                </el-row>
              </el-form>
            </el-col>
          </el-row>
            <div class="next-btn">
                <el-button type="success" icon="el-icon-right" round @click="checktable5">Next</el-button>
            </div>
        </el-tab-pane>

        <el-tab-pane label="Submit" :disabled="dis5" name="5">
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
import $ from 'jquery'

import { mapActions } from "vuex";
export default {
  name: "PropertyRegistration",
    props: {
        areat: {
            type: String,
            default: 'm2'
        }
    },
  components: {
    Header,
    GooglePlacesAutocomplete,
  },
    mounted(){
        $("#back-btn").hover(function(event) {
            $(this).stop().animate({"margin-left": "10px"}, 300);
            $(this).next(".bottom-line").stop().animate({"width": "100px"}, 300);
        });

        $("#back-btn").mouseleave(function(event) {
            $(this).stop().animate({"margin-left": "0"}, 300);
            $(this).next(".bottom-line").stop().animate({"width": "0"}, 300);
        });

        $("#back-btn2").hover(function(event) {
            $(this).stop().animate({"margin-left": "10px"}, 300);
            $(this).next(".bottom-line").stop().animate({"width": "100px"}, 300);
        });

        $("#back-btn2").mouseleave(function(event) {
            $(this).stop().animate({"margin-left": "0"}, 300);
            $(this).next(".bottom-line").stop().animate({"width": "0"}, 300);
        });
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

    const checkStart = (rule, value, callback) => {
        const intReg = /^[0-9]+$/;
        if (!intReg.test(value)) {
            callback(new Error("Please input an integer"));
        } else if(value > parseInt(this.form5.price)) {
            callback(new Error("Minimum price should smaller than reserved price!"));
        }
        else{
            callback();
        }
    };

    return {
        dis1:true,
        dis2:true,
        dis3:true,
        dis4:true,
        dis5:true,
        unread :'',
      place: null,
      inputDisable:true,
      dialogImageUrl: "",
      dialogVisible: false,
      hasLogin: false,
      activateIndex: '0',
        options: [{
            value: 'Apartment',
            label: 'Apartment'
        }, {
            value: 'Unit',
            label: 'Unit'
        }, {
            value: 'House',
            label: 'House'
        }, {
            value: 'Studio',
            label: 'Studio'
        },
           { value: 'Commercial',
          label: 'Commercial'
           }
        ],
        pickerOptions: {
            // disabledDate(time) {
            //     return parseInt(time.getTime()) < Date.now()
            // }
        },
        form:{},
        form1:{
            country:'',
            address: "",
            suburb: "",
            state: "",
            postcode: "",
        },
        form2: {
            type:'',
            bathroomNum: '',
            bedroomNum: '',
            garageNum: '',
            area: '',
        }
      ,
        form3:{
            kw_position: [],
            kw_details:[],
            description:'',
        },
        form4:{
            hasupload: 0,
            imageUrl: [],
            imageRaw: [],
        },
        form5: {
            minimumPrice:'',
            Auction:true,
            daterange:[],
            price: "",
            coord:'',
      },
      rules: {
        bedroomNum:[{ required: true, message: " Please enter bedroom number", trigger: "blur"},{validator:checkInt, trigger: "blur" },],
        garageNum:[{ required: true, message: " Please enter garage number", trigger: "blur"},{validator:checkInt, trigger: "blur" },],
        bathroomNum:[{ required: true, message: " Please enter bathroom number", trigger: "blur"},{validator:checkInt, trigger: "blur" },],
        address: [{ required: true, message: " Please enter address", trigger: "blur" },],
        suburb: [{ required: true, message: " Please enter suburb", trigger: "blur" },],
          type: [{ required: true, message: " Please select property type!", trigger: "blur" },],
          // imageUrl: [{ required: true, message: " Please upload cover photo", trigger: "blur" },],
        state: [{ required: true, message: "Please enter state", trigger: "blur" },],
          // room: [{ required: true, message: "Please input room numbers", trigger: "blur" },],
        postcode: [{required: true, message: " Please enter postcode", trigger: "blur",},],
        area: [{ required: true, message: " Please enter area", trigger: "blur"},{validator:checkInt, trigger: "blur" },],
        daterange: [{required: true, message: " Please enter start date", trigger: "blur",},],
        country: [{required: true, message: " Please enter country", trigger: "blur",},],
        price: [{required: true, message: " Please enter price", trigger: "blur"}, {validator: checkInt,trigger: "blur" },],
          minimumPrice: [{required: true, message: " Please enter start price", trigger: "blur"}, {validator: checkStart,trigger: "blur" },],
      },
    };
  },

  created() {
    this.username = localStorage.getItem("username");
    // this.username ='123';
    // this.username = this.$store.state.username;
    if (this.username !== null) {
      this.hasLogin = true;
      this.avatar = localStorage.getItem("avatar");
        this.$axios
            .get('/notification/unread')
            .then(response => {
                if (response.data.code === 200) {
                    this.unread = response.data.result;
                }else if(response.data.code === 400){
                    this.$message.error(response.data.msg);
                }else{
                    console.log(response.data.msg);
                }
            })
            .catch(function (error) {
                this.$message.error(error);
            });

    }
    else{
        this.$message.error("You should login first!");
        this.$router.push("/login");
    }
      // this.avatar ='https://ss1.bdstatic.com/70cFuXSh_Q1YnxGkpoWK1HF6hhy/it/u=2931000230,881740929&fm=11&gp=0.jpg'


  },
  methods: {
    ...mapActions(["logout"]),
    handleCommand(command) {
      switch (command) {
        case "profile":
          this.$router.push("/profile");
          break;
      case "property":
          this.$router.push("/propmag");
          break;
        case "auction":
          this.$router.push("/auction");
          break;
        case "notification":
            this.$router.push("/notice");
          break;
        case "logout":
          this.$axios.post('/user/logout')
                  .then((response) => {
                    if (response.status >= 200 && response.status < 300){
                      if (response.data.code === 200){
                        this.logout();
                          this.$router.replace("/");
                      }else if(response.data.code === 400){
                          this.$message.error(response.data.msg);
                      }else{
                          console.log(response.data.msg);
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
        this.$message.error("Only accept JPG、JPEG、PNG!");
      }
      if (!isLt2M) {
        this.$message.error("Image size can not larger than 2MB!");
      }
      return isImage && isLt2M;
    },

    imgBroadcastChange(file) {
        this.form4.hasupload = this.form4.hasupload + 1;
      this.form4.imageRaw.push(file.raw);
      this.form4.imageUrl.push(URL.createObjectURL(file.raw));
    },
    exceedTips: function () {
      this.$message.error("Maximum 10 photos.");
    },
      checktable1(){
          this.$refs["form1"].validate((valid) =>{
              if (valid) {
                  this.dis1 = false;
                  this.activateIndex = '1';
              }
          else{
              this.$message.error("Please complete the form.");
          }
      })
      },
      checktable2(){
          this.$refs["form2"].validate((valid) =>{
              if (valid) {
                  this.dis2 = false;
                  this.activateIndex = '2';
              }
              else{
                  this.$message.error("Please complete the form.");
              }
          })
      },
      checktable3(){
          this.$refs["form3"].validate((valid) =>{
              if (valid) {
                  this.dis3 = false;
                  this.activateIndex = '3';
              }
              else{
                  this.$message.error("Please complete the form.");
              }
          })
      },
      checktable4(){
          if (this.form4.hasupload !== 0) {
                  this.dis4 = false;
                  this.activateIndex = '4';
              }
              else{
                  this.$message.error("Please upload the photos.");
              }
          },
      checktable5(){
          this.$refs["form5"].validate((valid) =>{
              if (valid) {
                  this.dis5 = false;
                  this.activateIndex = '5';
              }
              else{
                  this.$message.error("Please complete the form.");
              }
          })
      },

    submit() {
        // console.log(this.form5.daterange[0]);
        // console.log(this.form5.daterange[0]/1000);
          let data = new FormData();
            data.append('address', this.form1.address);
            data.append('suburb', this.form1.suburb);
            data.append('state', this.form1.state);
            data.append('postcode', this.form1.postcode);
            data.append('country', this.form1.country);

          data.append('bathroomNum', this.form2.bathroomNum);
          data.append('bedroomNum', this.form2.bedroomNum);
          data.append('garageNum', this.form2.garageNum);
          data.append('type', this.form2.type);
          data.append('area', this.form2.area);

          data.append('position', this.form3.kw_position);
          data.append('detail', this.form3.kw_details);
          data.append('description', this.form3.description);

          if(!this.form5.Auction){
              data.append('auction', this.form5.Auction);
          }else{
              data.append('startdate', this.form5.daterange[0]);
              data.append('enddate', this.form5.daterange[1]);
              data.append('price', this.form5.price);
              data.append('minimumPrice', this.form5.minimumPrice);
              data.append('auction', this.form5.Auction);
          }

            // console.log(data);
            // added by Raymond
            data.append("lat", this.place.geometry.location.lat(this.place));
            data.append("lng", this.place.geometry.location.lng(this.place));


          this.form4.imageRaw.forEach(function (file) {
                    data.append('photos', file, file.name);
                 });

          this.$axios.post('/property/registration', data)
                  .then((response) => {
                    if (response.status >= 200 && response.status < 300) {
                      if(response.data.code === 200){
                        this.$message.success("Property register successful");
                        this.$router.replace("/propmag");
                      }else if(response.data.code === 400){
                          console.log('there')
                          this.$message.error(response.data.msg);
                          location.reload()
                      }
                    } else{
                      console.log(response.msg);
                    }
                  })
                  .catch((res) => {
            console.log('error', res);
            this.$message.error('Property Register Error');
          });

    },
      handleRemove(file, fileList) {
          const IMG = file.raw;
          const INDEX = this.form4.imageRaw.indexOf(IMG);
          this.form4.imageRaw.splice(INDEX, 1);
          this.form4.imageUrl.splice(INDEX, 1);
          this.form4.hasupload = this.form4.hasupload - 1;
          console.log(file, fileList);
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
        this.$set(this.form1,'address',"");
        this.$set(this.form1,'postcode',"");
        this.$set(this.form1,'suburb',"");
        this.$set(this.form1,'state',"");
        this.inputDisable = true;
        return ;
      }

      this.inputDisable = false;

      let postcode, street,suburb, state;
      let place_info = this.place.formatted_address.split(",");

      let country = place_info[place_info.length-1].trim();

      if(country!=="Australia"){
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
      this.$set(this.form1,'address',street.trim());
      this.$set(this.form1,'postcode',postcode.trim());
      this.$set(this.form1,'suburb',suburb.trim());
      this.$set(this.form1,'state',state.trim());
      this.$set(this.form1,'country',country.trim());
      return;
    },

      ['form2.area'](val) {
          this.$nextTick(() => {
              this.form2.area = val.replace(/\D/g,'');
          });
      },
      ['form2.bedroomNum'](val) {
          this.$nextTick(() => {
              this.form2.bedroomNum = val.replace(/\D/g,'');
          });
      },
      ['form2.bathroomNum'](val) {
          this.$nextTick(() => {
              this.form2.bathroomNum = val.replace(/\D/g,'');
          });
      },
      ['form2.garageNum'](val) {
          this.$nextTick(() => {
              this.form2.garageNum = val.replace(/\D/g,'');
          });
      },
      ['form5.price'](val) {
          this.$nextTick(() => {
              this.form5.price = val.replace(/\D/g,'');
          });
      },
      ['form5.minimumPrice'](val) {
          this.$nextTick(() => {
              this.form5.minimumPrice = val.replace(/\D/g,'');
          });
      },


  },
};
</script>

<style scoped lang="scss">
.next-btn{
    float:right;
    margin-bottom:10px;
}
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

.btn{
  color:black;
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

.google-map {
    span {
        color:black;
        border: 1px solid rgba(138, 138, 138, 0.4);
    }
}


    .el-checkbox {
        display: block;
    }


</style>
