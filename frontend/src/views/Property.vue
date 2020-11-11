<template>
    <div class="property">
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
                        <el-dropdown-item command="profile" icon="el-icon-user-solid"> My profile</el-dropdown-item>
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
        <template v-if="!this.notFound && !this.hasEnded">
        <el-row type="flex" justify="center" style="background-color:#fff;box-shadow: inset 0px 15px 20px -15px rgba(70,92,132,0.45);">
            <el-col :span="16" style="margin: 0 50px 20px 30px; border:1px solid #ccc">
                <h1 style="margin: 50px 50px 0 50px">{{ propInfo.address }}</h1>
                <section style="margin: 15px 50px 0 50px">
<!--                    <h1>{{ propInfo.address }}</h1>-->
                    <el-row class="banner" :class="addStatusColor(propInfo.status)">
                        <h5 style="padding: 5px;">{{ time }}</h5>

                    </el-row>
                    <el-carousel :interval="5000" arrow="always" :height="cheight">
                        <el-carousel-item v-for="pic in propInfo.photos" :key="propInfo.pid">
                            <img :src="pic"  width="100%" height="100%" alt=""/>
                        </el-carousel-item>
                    </el-carousel>
                    <el-row>
                    </el-row>
                </section>

                <el-row type="flex" justify="center" style="margin: 15px 50px 0 50px">
                    <el-col :span="12" >
                        <div class="bid">
                            <span v-if="propInfo.status==='R' || parseInt(propInfo.bidderNum) === 0" style="font-size:15px">Guide Bid</span>
                            <span v-else style="font-size:15px">Latest Bid</span>
                            ${{ propInfo.latestPrice | numFormat }}
                        </div>
                    </el-col>
                        <el-col :span="12">
                        <div class="bidder">
                            {{ propInfo.bidderNum }}  <span style="font-size:15px">Bidders</span>
                        </div>
                    </el-col>
                </el-row>


                <section style="margin: 15px 50px 0 50px">
                    <el-row>
                    <el-col >
<!--                    <h3>Details</h3>-->
                        <h3></h3>
                    <el-row type="flex" justify="left"  style="margin-bottom: 10px">
<!--                        <el-col :span="4">-->
                            <el-col class="tag-wrap2" :span="4">
                                <img src="@/assets/bath.png" alt="" />
                                <span> {{ propInfo.bathroomNum}} <span style="font-size:15px">Baths</span> </span>

                            </el-col>
                        <el-col class="tag-wrap2" :span="4" >
                            <img src="@/assets/bed.png" alt="" />
                            <span> {{ propInfo.bedroomNum}} <span style="font-size:15px">Beds</span></span>
                        </el-col>

                        <el-col class="tag-wrap2" :span="4">
                            <img src="@/assets/parking.png" alt="" />
                            <span> {{ propInfo.garageNum}} <span style="font-size:15px">Garages</span></span>
                        </el-col>

                        <el-col class="tag-wrap2" :span="4">
                            <img src="@/assets/bx-area.png" alt="" />
                            <span> {{ propInfo.area}} <span style="font-size:15px">m2</span></span>
                        </el-col>

                        <el-col class="tag-wrap2" :span="4">
                            <img src="@/assets/type.png" alt="" />
                            <span> {{ propInfo.type}} </span>
                        </el-col>

                    </el-row>


                        <el-row type="flex" style="margin: 20px 0;">
                    <p>{{ propInfo.description }}</p>
                        </el-row>

                        <el-row type="flex" style="margin-bottom: 10px;">
<!--                        <el-tag class='tag1' v-for="tag in propInfo.position.split(',')" effect="plain" :key="tag.id">{{ tag }}</el-tag>-->
                            <p class='tag-wrap' v-for="tag in (propInfo.position||'').split(',')" >{{ tag }}</p>
                            <p class='tag-wrap3' v-for="tag in (propInfo.detail||'').split(',')" >{{ tag }}</p>
                        </el-row>

<!--                        <el-row type="flex" style="margin: 20px 0;">-->
<!--                            <p class='tag-wrap3' v-for="tag in propInfo.detail.split(',')" >{{ tag }}</p>-->
<!--&lt;!&ndash;                        <el-tag v-for="tag in propInfo.detail.split(',')" :key="tag.id">{{ tag }}</el-tag>&ndash;&gt;-->
<!--                        </el-row>-->
                    </el-col>
                    </el-row>

                        <el-row style="margin-bottom:50px">
                        <el-col >
                            <h3>Map</h3>
                            <div class="map" id="map">
                                <GmapMap
                                        :center="center"
                                        :zoom="15"
                                        map-type-id="roadmap"
                                        style="height: 300px"
                                >
                                    <GmapMarker
                                            v-for="m in markers"
                                            :key="m"
                                            :position="m.position"
                                            :clickable="true"
                                            :draggable="true"
                                    />
                                </GmapMap>
                            </div>
                        </el-col>
                    </el-row>
                </section>


                <section v-show="propInfo.status ==='A'" style="margin: 0 50px 0 50px;height:400px;">
                    <h3>Bid History</h3>

                    <el-table :data="propInfo.history"
                              :max-height="300"
                              border
                              lazy
                              stripe
                              tooltip-effect="light"
                              style="overflow-y: scroll; "
                              :default-sort = "{prop: 'price', order: 'descending'}"
                    >

                        <template v-for="(item, index) in columns">
                            <el-table-column
                                    :key="index"
                                    :prop="item.prop"
                                    :label="item.label"
                                    :formatter="item.formatter"
                            >
                            </el-table-column>
                        </template>
                    </el-table>

                </section>

            </el-col>

            <el-col :span="5">
                <div class="info">
                    <el-row type="flex" justify="center" style="background-color: #4f6995;">
                        <h3 style="color:#f3f3f3">Seller</h3>
                    </el-row>
                <el-row type="flex" justify="center">
                    <el-avatar :size="70" :src="propInfo.avatar" style="margin-top:50px"></el-avatar>
                </el-row>

                    <el-row type="flex" justify="center" style="margin-top:20px">
                        <h4> {{ propInfo.firstname}} {{ propInfo.lastname}} </h4>
                    </el-row>

                    <el-row type="flex" justify="center" style="margin-top:20px">
                        <i class="el-icon-user-solid"><span> {{ propInfo.username}} </span></i>
                    </el-row>

                    <el-row type="flex" justify="center" style="margin-top:20px">
                        <i class="el-icon-phone"><span> {{ propInfo.phone}} </span></i>
                    </el-row>

                    <el-row type="flex" justify="center" style="margin-top:20px">
                        <i class="el-icon-message"><span> {{ propInfo.email}} </span></i>
                    </el-row>
                </div>

<!--                <el-button type="" :disabled="true" style="margin-top: 10px;color:#173b77;font-size: 20px;background-color:#a0b9df; width:100%"-->
<!--                >{{ propInfo.bidderNum }} Bidders</el-button>-->
                <template v-if="this.username !== null">
                <template v-if="username !== propInfo.username">
                    <div v-if="this.propInfo.rab !== null">
    <!--                    <h3>Place new bid</h3>-->
                        <div class="new-bid-wrap" >
                            <el-form
                                    class="bidform"
                                    ref="bidform"
                                    :model="bidform"
                                    :rules="rules"
                            >
                            <el-col :span="21">
                            <el-form-item  prop="newBid">
                            <el-input v-model="bidform.newBid" :disabled="this.propInfo.status==='R'" maxlength="10" placeholder="Place New Bid" >
                                <i slot="suffix" class="input-slot">{{ bidform.newBid | numFormat }} A$</i>
                            </el-input>
                            </el-form-item>
                            </el-col>
                                <el-col :span="3">
                            <el-button class='wrap-button' type="primary" icon="el-icon-plus"  @click="addNewBid"></el-button>
                                </el-col>
                            </el-form>

                        </div>

                        <p v-show="propInfo.status ==='A'" style="color:rgba(78,102,146,0.35)">Your Current Bid is $ {{ propInfo.highestPrice | numFormat }}</p>
                    </div>

                    <div v-else style="margin-top: 20px">
                        <el-button type=""
                                   class="btn"
                                   @click="Bidreg"
                                   icon="el-icon-right"

                        >Register as a bidder</el-button>
                    </div>
                    </template>
                <template v-else>
                    <div class="new-bid-wrap">
                    <el-button type="" :disabled="true" style="color:#f1f1f1;font-size: 20px;background-color:#3b4c73; width:100%">You are the Seller!</el-button>
                    </div>
                </template>
                </template>

                <template v-else>
                    <el-button style="width:100%;margin-top: 20px" @click="goto('login')">Login to Bid</el-button>
<!--                    <el-button style="width:100%" @click="test">test</el-button>-->
                </template>

                <h5 style=" margin-top:100px;">Similar</h5>
                <el-row type="flex" justify="center">
                    <div style="width:100%">
                        <el-col  v-for="item in propInfo.recommendations" :key="item.aid ">
                            <div class="recomd"  @click="goDetails(item)">
                                <el-row :gutter="20">
                                    <el-col :span="8" style="padding:0;margin:0">
                                        <img
                                                style="height:80px;width:100%;"
                                                :src="item.photo"
                                                 alt=""/>

                                    </el-col>

                                    <el-col  :span="16">
                                <el-row type="flex" justify="left" style="margin:10px 0;">
                                    <span>{{item.address}}, {{item.suburb}}, {{item.state}} {{item.postcode}}</span>
                                </el-row>
                                <el-row type="flex" justify="left" style="margin:10px 0;">
                                    <el-col :span="8">
                                        <i class="fas fa-bath" style="margin-right: 5px"></i> {{ item.bathroomNum}}
                                    </el-col>
                                    <el-col :span="8">
                                        <i class="fas fa-bed" style="margin-right: 5px"></i> {{ item.bedroomNum }}
                                    </el-col>
                                    <el-col :span="8">
                                        <i class="fas fa-car" style="margin-right: 5px"></i> {{ item.garageNum }}
                                    </el-col>

<!--                                    <el-col :span="4">-->
<!--                                        <i class="fas fa-home" style="margin-right: 5px"></i> {{ item.area }} ㎡-->
<!--                                    </el-col>-->
<!--                                    <el-col :span="8">-->
<!--                                        <i class="fas fa-tags" style="margin-right: 5px"></i> {{ item.type }}-->
<!--                                    </el-col>-->
                                </el-row>
                                    </el-col>

                                </el-row>
                            </div>
                        </el-col>
                    </div>
                </el-row>

            </el-col>
        </el-row>

        <el-dialog
                class="dialog"
                type="flex"
                title="Payment"
                :visible.sync="bidderFlag"
                :before-close="closedialog"
                style="position:absolute;left:15%; right:15%;"
                >
            <template>


                <el-form>
                    <el-tabs v-model="activateIndex" :tab-position="'left'" >
                        <el-tab-pane label="Select Card" name="0" >
                            <el-row v-show="!addNewCard" type="flex" justify="center">
                                <el-col :span="15">
                                        <el-row>
                                            <el-radio-group v-model="selectCard" >
                                                <el-radio
                                                        class="radio"
                                                        :label="item.paymentId"
                                                        :key="item.paymentId"
                                                        :value="item.paymentId"
                                                        v-for="item in cards"

                                                        border
                                                >
                                                    {{showCard(item.cardNumber)}}
                                                </el-radio>
                                            </el-radio-group>
                                        </el-row>
                                        <el-row>
                                            <div class="btns3">
                                            <el-col class="el-radio" style="height:40px">
                                                <span class="add-btn"
                                                      @click="checktable1"
                                                        >
                                                    <div style="margin:12px 0;text-align: center">
                                                    <i class="el-icon-plus">
                                                    </i> Add New Card
                                                    </div>

                                                </span>

                                            </el-col>
                                            </div>
                                        </el-row>
                                    <el-row>
                                        <div class="btns2">
                                            <el-col>
                                                <span  style="padding:2px 5px;" @click="checktable2"><i class="el-icon-right"></i> Next</span>

                                            </el-col>
                                        </div>
                                    </el-row>

                                </el-col>
                            </el-row>

                            <el-row v-show="addNewCard" type="flex" justify="center">
                                <el-col>
                                    <el-form
                                            class="form"
                                            ref="form2"
                                            :model="form2"
                                            :rules="rules"
                                    >
                                        <el-form-item prop="name">
                                            <el-input v-model="form2.name" placeholder="Name" clearable></el-input>
                                        </el-form-item>
                                        <el-form-item prop="cardNumber">
                                            <el-input v-model="form2.cardNumber"  maxlength="19"  placeholder="Card Number"></el-input>
                                        </el-form-item>
                                        <el-row>
                                            <el-col :span=12>
                                                <el-form-item prop="expiredDate">
                                                    <el-input v-model="form2.expiredDate" placeholder="MM/YY"  maxlength="5"></el-input>
                                                </el-form-item>
                                            </el-col >
                                            <el-col :span=12>
                                                <el-form-item prop="cvc" >
                                                    <el-input v-model="form2.cvc" placeholder="CVC" maxlength="3"></el-input>
                                                </el-form-item>
                                            </el-col>
                                        </el-row>
                                        <el-row>
                                            <div class="btns2">
                                                <span  style="padding:2px 5px;" @click="checktable3"><i class="el-icon-plus"></i> Add</span>
                                            </div>

                                            <div class="btns2">
                                                <span  style="padding:2px 5px;" @click="checktable4"><i class="el-icon-back"></i> Back</span>
                                            </div>
                                        </el-row>
                                    </el-form>
                                </el-col>
                            </el-row>
                        </el-tab-pane>




                        <el-tab-pane label="Submit" name="2" >
                            <el-col :span="20">
                            <el-row type="flex" justify="center">

                                <el-form
                                        class="form"
                                        ref="form3"
                                        :model="form3"
                                        :rules="rules"

                                >
                                    <el-form-item  prop="initPrice">
                                        <h6>Input your initial bid to proceed.</h6>
                                        <el-input v-model="form3.initPrice" maxlength="11"  placeholder="Initial Price"></el-input>
                                        <p>Your initial bid is ${{ form3.initPrice | numFormat }}.</p>
                                    </el-form-item>
                                </el-form>
                            </el-row>
                            <el-row>
                                <div class="btns2">
                                    <span  style="padding:2px 5px;" @click="submitCard"><i class="el-icon-right"></i> Submit</span>
                                </div>

                            </el-row>
                            </el-col>
                        </el-tab-pane>
                    </el-tabs>
                </el-form>
            </template>

        </el-dialog>
        </template>

        <template v-if="this.notFound && !this.hasEnded">
            <div >

                <div class="img-404">
                    <img src="../assets/sad.png" alt="" >
                </div>

            <el-row type="flex" justify="center">
                <span style="
                margin-top:10px;
                font-size: 100px;
               color:#475669;
                font-weight: bold">404</span>
            </el-row>
            <el-row type="flex" justify="center">
                <span style="font-size:20px;color:rgba(89,108,132,0.88);">Oops..This auction can not be found.</span>
            </el-row>
            </div>
        </template>

        <template v-if="this.hasEnded">
            <div >

                <div class="img-404">
                    <img src="../assets/sad.png" alt="" >
                </div>

                <el-row type="flex" justify="center">
                <span style="
                margin-top:10px;
                font-size: 100px;
               color:#475669;
                font-weight: bold">403</span>
                </el-row>
                <el-row type="flex" justify="center">
                    <span style="font-size:20px;color:rgba(89,108,132,0.88);">Oops..This auction has ended.</span>
                </el-row>
            </div>
        </template>

    </div>
</template>

<script>
    import Header from "@/components/Header.vue";
    import { mapActions } from "vuex";
    import dayjs from "dayjs";
    import $ from 'jquery'
    import numFormat from "../utils/numFormat";

    var customParseFormat = require("dayjs/plugin/customParseFormat");
    dayjs.extend(customParseFormat);

    export default {
        name: "Home",
        props: {
            cheight: {
                type: String,
                default: '500px'
            },
        },
        components: {
            Header,
        },

        data() {
            const checkCVC = (rule, value, callback) => {
                const cvcreg = /^\d{3}$/;
                if (!cvcreg.test(value)) {
                    callback(new Error("Please enter the correct cvc code"));
                } else {
                    callback();
                }
            };

            const checkDate = (rule, value, callback) => {
                const datereg = /^.{5,}$/;
                if (!datereg.test(value)) {
                    callback(new Error("Please enter the correct expiry date"));
                } else {
                    callback();
                }
            };

            const checkCard = (rule, value, callback) => {
                const cardreg =  /^.{19,}$/;
                if (!cardreg.test(value)) {
                    callback(new Error("Please enter the correct card number"));
                } else {
                    callback();
                }
            };

            const checkinit = (rule, value, callback) => {
                let price = value.replace(/,/g, "");
                const pricereg = /^\d{10}$/;
                    if (!pricereg.test(price)) {
                        if (parseInt(price) < parseInt(this.propInfo.latestPrice)) {
                            callback(new Error("Should larger than the latest price"));
                        } else {
                            callback();
                        }
                    }
            };

            const checkNewBid = (rule, value, callback) => {
                const pricereg = /^\d{10}$/;
                if (!pricereg.test(value)) {
                    if (parseInt(value) < parseInt(this.propInfo.latestPrice)) {
                        callback(new Error("Should larger than the latest price"));
                    } else {
                        callback();
                    }
                }
            };


            return {
                unread:'',
                activateIndex:'0',
                websock: null,
                dis1:false,
                dis2:true,
                notFound:false,

                id:'',
                username:'',
                hasLogin: false,
                isBidder: false,
                bidderFlag: false,
                timeFlag: false,
                addNewCard:false,
                rabId:'',
                bidform:{
                    newBid: '',
                },

                newPlacedBid:'',
                tipError: false,
                time: '',
                hasEnded:false,
                cards:[
                //     {
                //     paymentId:'12',
                //     name:'Tom',
                //     cardNumber:'4321432143214321',
                //     cvc:'123',
                // },
                //     {
                //         paymentId:'11',
                //         name:'Bob',
                //         cardNumber:'1234123412341234',
                //         cvc:'012',
                //     },
                //     {
                //         paymentId:'15',
                //         name:'Tom',
                //         cardNumber:'4321432143214321',
                //         cvc:'123',
                //     }
                ],

                detail_tags:[],
                position_tags:[],
                selectCard:'',
                // center:{lat:-33.9175679,lng:151.2255712},
                lat :'',
                lng:'',
                center: {},
                initialBid:'',
                vdaH:'',
                startTimer:false,
                // markers:[{position:{lat:-33.9175679,lng:151.2255712}}],
                markers:[{position:{},}],
                columns: [
                    {prop: 'time', label: 'Time',width: '300',formatter: this.showTime_table},
                    {prop: 'uid', label: 'UID', width: '300'},
                    {prop: 'username', label: 'User', width: '300'},
                    {prop: 'price', label: 'Current Bid',formatter: this.formatPrice,sortable:true}
                ],
                propInfo: {
                    id: '',
                    aid:'12',
                    rab:null,
                    // endDate: new Date(2000, 10, 10, 10, 10),
                    username:'',
                    address: '',
                    enddate:'',
                    status:'',
                    startdate:'',
                    avatar:'',
                    bidderNum:'',
                    latestPrice:'',
                    info: '',
                    bedroomNum:'',
                    bathroomNum:'',
                    garageNum:'',
                    phone: '',
                    email:'',
                    position: '',
                    timer:'',
                    detail: '',
                    photos: [],
                    description: '',
                    history:[
                        // {
                        //     time: 1603981349 ,
                        //     uid:123,
                        //     user:'UMR',
                        //     price: '123123123',
                        // },
                        // {
                        //     time: 160398890,
                        //     user:'ooo',
                        //     uid:345,
                        //     price: '1110001',
                        // },
                        // {
                        //     time: 1603890349,
                        //     user:'TSF',
                        //     uid:456,
                        //     price: '123123',
                        // },
                        //


                    ],
                    firstname:'',
                    lastname:'',
                    highestPrice:'',
                    recommendations:[
                        // {
                        //     photo:'https://ss1.bdstatic.com/70cFvXSh_Q1YnxGkpoWK1HF6hhy/it/u=513256393,3533342652&fm=15&gp=0.jpg',
                        //     address:'2 gearin alley',
                        //     aid:15,
                        //     bathroomNum:2,
                        //     bedroomNum:2,
                        //     garageNum:2,
                        //     state:'NSW',
                        //     suburb:'Mascot',
                        // },
                        //
                        // {
                        //     photo:'',
                        //     address:'2 gearin alley',
                        //     aid:11,
                        //     bathroomNum:2,
                        //     bedroomNum:2,
                        //     garageNum:2,
                        //     state:'NSW',
                        //     suburb:'Mascot',
                        // }
                        // ,{
                        //     photo:'',
                        //     address:'2 gearin alley',
                        //     aid:15,
                        //     bathroomNum:2,
                        //     bedroomNum:2,
                        //     garageNum:2,
                        //     state:'NSW',
                        //     suburb:'Mascot',
                        // }
                    ],
                },
                form2: {
                    name: '',
                    cardNumber: '',
                    expiredDate: '',
                    cvc: '',
                    initPrice:'',
                },
                form3:{
                    initPrice:'',
                },
                form:{},

                rules: {
                    initPrice:[{required: true, message: " Please enter init price", trigger: "blur",}, {validator:checkinit, trigger: "blur" }],
                    name: [{required: true, message: " Please enter name", trigger: "blur",},],
                    cardNumber: [{required: true, message: " Please enter cardNumber", trigger: "blur",}, {validator:checkCard, trigger: "blur" },],
                    expiredDate: [{required: true, message: " Please enter expired date", trigger: "blur"}, {validator:checkDate, trigger: "blur" },],
                    cvc: [{required: true, message: " Please enter cvc", trigger: "blur",},{validator:checkCVC, trigger: "blur" }],
                    // initPrice: [{required: true, message: " Please initial price", trigger: "blur",},{validator:checkinit, trigger: "blur" }],
                    newBid: [{required: true, message: " Please enter new bid price", trigger: "blur"}, {validator: checkNewBid,trigger: "blur" },],
                },
            };
        },

        created() {
            this.username = localStorage.getItem("username");
            // this.username= '123';
            this.id = this.$route.query.id;
            let h = document.documentElement.clientHeight  || document.body.clientHeight;
            this.vdaH = h - 147 + 'px';
            if (this.username !== null) {
                this.hasLogin = true;
                this.avatar = localStorage.getItem('avatar');
                this.$axios
                    .get('/notification/unread')
                    .then(response => {
                        if (response.data.code === 200) {
                            this.unread = response.data.result;
                        }
                    })
                    .catch(function (error) {
                        this.$message.error(error);
                    });
            }
            var that = this;
            this.$axios
                .get('/auction/information/' + this.id)
                .then(response => {
                    if (response.status >= 200 && response.status < 300) {
                        if (response.data.code === 200) {
                            this.propInfo = response.data.result;
                            this.initWebSocket();
                            // this.isBidder = response.data.result.isBidder,
                            //     this.propInfo.enddate = parseInt( this.propInfo.enddate ) + 11*60*60*1000;
                            this.lat = parseFloat(response.data.result.lat),
                                this.lng = parseFloat(response.data.result.lng),
                                this.position_tags = response.data.result.position,
                                this.detail_tags = response.data.result.detail,
                                this.center = {
                                    lat: this.lat,
                                    lng: this.lng
                                },
                                this.markers = [{
                                    position: {
                                        lat: this.lat,
                                        lng: this.lng
                                    },
                                }]
                            this.startTimer = true;
                        }
                    }
                })
                .catch(function (error) {

                     if (error.response.status ===404) {
                        that.notFound = true;
                    }
                    if (error.response.status ===403) {
                        that.hasEnded = true;
                    }
                });

            // this.notFound = true;
        },

        watch: {
            ['form2.cardNumber'](val) {
                this.$nextTick(() => {
                    this.form2.cardNumber = val.replace(/\D/g,'').replace(/....(?!$)/g,'$& ');
                });
            },

            ['form2.expiredDate'](val) {
                this.$nextTick(() => {
                    this.form2.expiredDate = val.replace(/\D/g,'').replace(/..(?!$)/g,'$&\/');
                });
            },
            ['form3.initPrice'](val) {
                this.$nextTick(() => {
                    this.form3.initPrice = val.replace(/\D/g,'')
                });
            },
            ['bidform.newBid'](val) {
                this.$nextTick(() => {
                    this.bidform.newBid = val.replace(/\D/g,'')
                });
            },
        },

        mounted() {
                this.timer = setInterval(() => {
                    // if (this.timeFlag === true) {
                    //     clearInterval(this.timer);
                    // }
                    if(this.propInfo.status==='R'){
                        this.countDown(this.propInfo.startdate, dayjs().valueOf());
                    }else if(this.propInfo.status==='A'){
                        this.countDown(this.propInfo.enddate,this.propInfo.startdate);
                    }

                }, 1000);


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
                        this.$router.push("/auctionmag");
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
                                        location.reload();
                                        // this.$router.replace("/");
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

            goDetails (item) {
                // console.log('yes')
                this.$router.replace(
                    {
                        path: '/auction',
                        query:
                            {
                                id: item.aid,
                            }
                    }
                )
                location.reload()
            },

            checktable1(){
                this.addNewCard = true;
            },

            checktable2(){
                this.activateIndex = '2';
            },

            checktable4(){
                this.form2.cvc = '';
                this.form2.name = '';
                this.form2.cardNumber = '';
                this.form2.expiredDate = '';
                this.addNewCard = false;
            },

            checktable3(){
                this.$refs["form2"].validate((valid) =>{
                    if (valid) {
                        let data = new FormData();
                        data.append('name', this.form2.name);

                        let card = this.form2.cardNumber.replace(/\s+/g, "");
                        data.append('cardNumber', card);
                        let date = this.form2.expiredDate.replace(/\//g, "");
                        data.append('expiryDate', date);
                        data.append('cvv', this.form2.cvc);

                        this.$axios
                            .post('/payment/add', data)
                            .then(response => {
                                let newCard = response.data.result;
                                this.cards.push({
                                    paymentId:newCard.paymentId,
                                    name:newCard.name,
                                    cardNumber:newCard.cardNumber,
                                    cvc:newCard.cvc,
                                    expiredDate:newCard.expiryDate});
                            })
                            .catch(function (error) {
                                console.log(error)
                            });
                        this.form2.cvc = '';
                        this.form2.name = '';
                        this.form2.cardNumber = '';
                        this.form2.expiredDate = '';

                        this.addNewCard = false;
                        this.activateIndex = '0';
                    }
                    else{
                        this.$message.error("Please complete the form.");
                    }
                })
            },
            closedialog(){
                this.bidderFlag = false;
                this.addNewCard = false;
                this.form2.cvc = '';
                this.form2.name = '';
                this.form2.cardNumber = '';
                this.form2.expiredDate = '';
                this.selectCard = '';
                this.form3.initPrice = '';
                this.activateIndex = '0';
            },


            addStatusColor(status) {
                switch(status) {
                    case 'R':
                        return 'status-not-start1';
                        break;
                    case 'A':
                        return 'status-process1';
                        break;
                    default:
                        break;
                }
            },


            countDown(endtime,startime) {
                let expiredTime = dayjs(endtime);
                let startTime = dayjs(startime);
                // console.log(expiredTime.format("YYYY-MM-DD HH:mm:ss"));
                // console.log(startTime.format("YYYY-MM-DD HH:mm:ss"));
                // let startTime = dayjs(new Date(2020, 11, 24, 17, 1));
                // let expiredTime = dayjs(new Date(2020, 11, 31, 17, 1));
                let nowTime = dayjs().valueOf();

                let diff = expiredTime.diff(nowTime) / 1000;
                let diff2 = expiredTime.diff(startTime) / 1000;
                // console.log(diff);
                // console.log(nowTime);
                // console.log(startTime);
                // console.log(diff2);

                let day = parseInt(diff / 3600 / 24);
                let hour = parseInt((diff / 3600) % 24);
                let minute = parseInt((diff / 60) % 60);
                let second = parseInt(diff % 60);

                // console.log(this.showTime(startTime))

                // if (diff2 > 0) {
                if(this.propInfo.status === 'R'){
                    // this.timeFlag = true;
                    let st = dayjs(endtime).format("YYYY-MM-DD HH:mm:ss");
                    this.time = `Will start at ${ this.showTime2(st) }`;
                    // if(diff2<=0){
                    //     // this.timeFlag = false;
                    //     clearInterval(this.timer);
                    //     // console.log('over');
                    //     // this.time = `This auction is Over!`;
                    //     location.reload()
                    // }
                }
                else{
                    if(diff>0){
                        this.time = `Time Left: ${day} Days: ${hour} Hours: ${minute} Mins: ${second} Secs `;
                    }else{
                        // this.timeFlag = false;
                        // console.log('over');
                        this.time = `This auction is Over!`;
                        location.reload()
                    }
                }


            },

            showTime(time){
                console.log(time)
                let st = dayjs(time).format("YYYY-MM-DD HH:mm:ss");
                return `${ st }`;
            },

            showTime_table(row,column){
                // console.log(row.time)
                let st = dayjs(row.time).format("YYYY-MM-DD HH:mm:ss");
                return `${ st }`;
            },

            showTime2(time){
                // let time = dayjs(day).format("YYYY-MM-DD HH:mm:ss");
                let MONTH =['Jan', 'Feb', 'Mar','Apr', 'May', 'Jun', 'Jul', 'Aug', 'Sep', 'Oct', 'Nov', 'Dec'];
                let day = dayjs(time).date();
                let mon = MONTH[dayjs(time).month()];
                let year = dayjs(time).year();
                let hour = dayjs(time).hour();
                let min = dayjs(time).minute();
                let formatTime = `${day} ${mon} ${year}, ${hour}:${min}`;
                return formatTime;
            },



            formatPrice(row,coloum) {
                // console.log(number)
                let temp =  row.price.toString().replace(/(\d)(?=(?:\d{3})+$)/g,'$1,')
                return `$ ${ temp }`;
            },


            addNewBid() {
                this.$refs["bidform"].validate((valid) => {
                        if (valid) {
                            this.$confirm("Are you sure place the new bid?", "tip", {
                                confirmButtonText: "Submit",
                                cancelButtonText: "Cancel",
                                type: "warning",
                            })
                                .then(() => {
                                    let data = new FormData();
                                    data.append('rabId', this.propInfo.rab);
                                    data.append('bidPrice', this.bidform.newBid);
                                    // console.log(this.bidform.newBid)
                                    this.$axios.post('/bid', data)
                                        .then((response) => {
                                            if (response.status >= 200 && response.status < 300) {
                                                if (response.data.code === 200) {
                                                    this.propInfo.highestPrice = this.bidform.newBid;
                                                    // this.newPlacedBid = this.newBid;
                                                    this.bidform.newBid = '';
                                                    this.$message({
                                                        type: "success",
                                                        message: "Place new bid successful!",
                                                    })
                                                    // location.reload();
                                                } else if (response.data.code === 400) {
                                                    this.$message.error(response.msg);
                                                } else {
                                                    this.$message.error(response.msg);
                                                    console.log(response.msg);
                                                }
                                            } else {
                                                this.$message.error(response.msg);
                                                console.log(response.msg);
                                            }
                                        })
                                        .catch((response) => {
                                            console.log('error ', response);
                                            this.$message.error('New Bid failed!');
                                        })

                                })
                                .catch(() => {
                                    this.$message({
                                        type: "info",
                                        message: "ERROR",
                                    });
                                });
                        }
           })
            },
            Bidreg() {
                this.bidderFlag = true;
                if(this.propInfo.rab === null){
                    this.$axios
                        .get('/payment/get/')
                        .then(response => {
                            this.cards = response.data.result;
                        })
                        .catch(function (error) {
                            console.log(error)
                        });
                }

                if(this.cards.length !== 0){
                    this.selectCard = this.cards[0].paymentId;
                }
            },

            submitCard(){
                    if(this.selectCard !== '') {
                        this.$refs["form3"].validate((valid) => {
                            if (valid) {
                                let data = new FormData();

                                data.append('aid', this.id);
                                data.append('registerTime', dayjs().valueOf().toString());
                                let price = this.form3.initPrice.replace(/,/g, "");
                                data.append('initPrice', price);
                                data.append('paymentId', this.selectCard)

                                this.$axios.post('/rab/register', data)
                                    .then((response) => {
                                        if (response.status >= 200 && response.status < 300) {
                                            if (response.data.code === 200) {
                                                this.propInfo.rab = response.data.result;
                                                this.$message.success("Register successful!");
                                                // location.reload();
                                                this.highestPrice = price;
                                            }
                                        } else if (response.data.code === 400) {
                                            this.$message.error(response.msg);
                                        } else {
                                            console.log(response.msg);
                                        }
                                    })
                                    .catch((res) => {
                                        console.log('error', res);
                                        this.$message.error('Error');
                                    });
                                this.addNewCard = false;
                                this.bidderFlag = false;
                            } else {
                                return false;
                            }
                        });
                    }else{
                        this.$message.error('You should select a card before submit.');
                    }
            },

            goto(name) {
                console.log(name);
                // this.$router.push({ name: name });
                this.$router.push(
                    {
                        name: name,
                        query:
                            {
                                id: this.propInfo.aid,
                            }
                    }
                )
            },

            // to update the highest bid
            initWebSocket(){
                const uri =  `ws://127.0.0.1:8070/auction/${this.propInfo.aid}`;
                this.websock = new WebSocket(uri);
                this.websock.onmessage = this.websocketonmessage;
                this.websock.onopen = this.websocketonopen;
                this.websock.onerror = this.websocketonerror;
                this.websock.onclose = this.websocketclose;
            },
            websocketonopen(){ //连接建立之后执行send方法发送数据
                let actions = {"test":"12345"};
                this.websocketsend(JSON.stringify(actions));
            },
            websocketonerror(){//连接建立失败重连
                this.initWebSocket();
            },

            websocketonmessage(e){ //数据接收
                let res = JSON.parse(e.data);
                if(res.refresh === true){
                    location.reload()
                }
                if(res.newBidder===true){
                    this.propInfo.bidderNum +=1;
                }
                this.propInfo.latestPrice = res.price;
                let Time = this.showTime(res.time);
                this.propInfo.history.push({time:Time, uid:res.uid, username:res.username, price:res.price});
                this.notice(res);
                if(res.overtime === true){
                    clearInterval(this.timer);
                    this.propInfo.enddate = this.propInfo.enddate + 2*1000*60;
                    this.timer = setInterval(() => {
                        this.countDown(this.propInfo.enddate,this.propInfo.startdate);
                    }, 1000);
                }
            },

            websocketsend(Data){//数据发送
                this.websock.send(Data);
            },
            websocketclose(e){  //关闭
                console.log('close',e);
            },

            test(){
                const h = this.$createElement;
                this.$notify({
                    title: 'Bid Update!',
                    dangerouslyUseHTMLString: true,
                    message:`User <strong>umr</strong> becomes the winner!\n<strong>Current bid:</strong> $123123123.\n<strong>Bid Time:</strong> ${this.showTime(dayjs().valueOf())}`
                });
            },

            notice(res) {
                const h = this.$createElement;
                this.$notify({
                    title: 'Bid Update!',
                    dangerouslyUseHTMLString: true,
                    message:`User <strong>${res.username}</strong> becomes the winner!\n<strong>Current bid:</strong> ${res.price | numFormat()}.\n<strong>Bid Time:</strong> ${this.showTime(res.time)}`
                });
            },
            showCard(card){
                var reg = /^(\d{4})\d+(\d{4})$/;
                return card.replace(reg, "$1 **** **** $2");
            }

        },
        destroyed() {
            this.websock.close() //离开路由之后断开websocket连接
        },
    };
</script>

<style lang="scss">
    .recomd{
        cursor:pointer;
        background-color: rgba(159, 185, 229, 0.12);
        margin-bottom:10px;
        padding: 10px 20px;
        width:100%;
        &:hover{
            background-color: rgba(193, 212, 240, 0.55);
            transition-duration: 0.5s;
        }
    }
    .newBid{
        padding:0;
    }
    .property{
        /*background-color: #aaaaaa;*/
    }
    .user {
        display: flex;
        align-items: center;
        p {
            margin-left: 10px;
        }
    }

    p {
        line-height: 2.5;
    }
    h3 {
        margin-top: 30px;
        margin-bottom: 10px;
    }

    .el-date-editor.el-input,
    .el-date-editor.el-input__inner {
        width: 100%;
    }
    .info{
        width:100%;
        height:500px;
        margin-top:50px;
        border:1px solid #ccc;
        /*box-shadow: 2px 1px 5px 4px #d5dbea;*/
    }
    .bid {
        padding: 15px;
        text-align: center;
        font-size: 25px;
        font-weight: bold;
        color: #fff;
        background-color: rgba(24, 64, 128, 0.82);
        /*border-radius: 5px;*/
        /*border-left: 15px solid #133264;*/
    }
    .bidder{
        padding: 15px;
        text-align: center;
        font-size: 25px;
        font-weight: bold;
        color: #184080;
        background-color: rgba(34, 91, 182, 0.25);
        /*border-radius: 5px;*/
        /*border-left: 15px solid #133264;*/
    }
    .countdownTime{
        width :80%;
        padding: 10px;
        text-align: center;
        /*background-color: #c8dbf9;*/
        border-radius: 5px;
        font-size: 15px;
    }
    .banner{
        width :100%;
        padding-top: 10px;
        align-items: center;
        text-align: center;
        background-color: #becdd6;
        /*border-radius: 5px;*/
        font-size: 10px;
    }

    .el-tag {
        margin-right: 20px;
    }
    .map {
        /*margin-top:50px;*/
        height: 100%;
        width: 100%;
        /*border: #4d5861 1px solid;*/
    }
    .new-bid-wrap {
        /*display: flex;*/
        /*align-items: center;*/
        width:98%;
        margin-top:20px;
        .wrap-button{
            /*margin-left: 30px;*/
        }
    }
    .btn{
        font-size: 20px;
        background-color:#173b77;
        width:100%;
        color:#f3f3f3;
        /*&:hover {*/
        /*    cursor: pointer;*/
        /*    transform: scale(1.02);*/
        /*}*/
    }
    .status-process1 {
        background-image: url("../assets/banner-bg-green.png");
    }

    .status-not-start1 {
        background-image: url("../assets/banner-bg-red.png");
    }
    .back-btn{
        cursor: pointer;
        position:relative;
        width:150px;
        /*border:1px solid #123123;*/
    }
    .back-btn .bottom-line{
        position: absolute;
        /*margin-left:40px;*/
        width: 0;
        height: 2px;
        left:0;
        bottom: -1px;
        background-color: #3b4c73;
    }

    .el-tag{
        padding: 0 10px !important;
        height:30px !important;
        border-radius: 0 !important;
        border-left:2px solid #133264 !important;
        border-bottom:0;
        border-right:0;
        border-top:0;
        color: rgba(24, 64, 128, 0.51) !important;
        font-size: 18px !important;
    }

    .el-dialog__header{
        padding:20px 20px 10px;
        background-color: #2f4764;
    }
    .el-dialog__title{
        color:white !important;
    }

    .el-table__header{
        font-size: 20px !important;
    }
    .table-header{
        background-color:#4f6995;
        color:white
    }

    .btns{
        cursor: pointer;
        position:relative;
        width:150px;
        font-size:15px;
        /*border:1px solid #123123;*/
        &:hover{
            transform:translateX(10px);
            transition-duration: 0.5s;
            /*border:2px solid #184080;*/

        }
    }
    .btns2{
        /*border:1px solid #123123;*/
        cursor: pointer;
        position:relative;
        /*width:150px;*/
        font-size:15px;
        float:right;
        margin:50px 5px;
        /*border:1px solid #123123;*/
        &:hover{
            transform:translateX(10px);
            transition-duration: 0.5s;
            /*border:2px solid #184080;*/

        }
    }
    .btns3{
        /*border:1px solid #123123;*/
        cursor: pointer;
        position:relative;
        /*width:150px;*/
        font-size:15px;
        /*float:right;*/
        /*border:1px solid #123123;*/
        &:hover{
            /*transform:translateX(10px);*/
            color: #005b9a;
            transition-duration: 0.5s;
            /*border:2px solid #184080;*/
            /*color: rgba(83, 109, 155, 0.42);*/

        }
    }

    .img-404
    {
        display: flex;
        justify-content: center;
        text-align: center;
            img{
            margin:100px 0 0 0;
            width: 5%;
            /*height: 10%;*/
            /*position:absolute;*/
            }
    }

    .el-radio{
        margin-left:0 !important;
    }

    .tag-wrap {
        margin-right: 20px;
        /*width: 150px;*/
        /*height: 60px;*/
        padding:0 20px;
        color: #004e85;
        border: 1px solid #c4ccd5;
        border-radius: 3px;
        font-weight: bold;
        font-size: 15px;
        display: flex;
        justify-content: center;
        align-items: center;

    }
    .tag-wrap3 {
        margin-right: 20px;
        /*width: 150px;*/
        /*height: 60px;*/
        padding:0 20px;
        background-color: rgba(0, 78, 133, 0.68);
        color: white;
        border: 1px solid #c4ccd5;
        border-radius: 3px;
        font-weight: bold;
        font-size: 15px;
        display: flex;
        justify-content: center;
        align-items: center;

    }

    .tag-wrap2 {
        margin-right: 30px;
        width: 100px;
        height: 60px;
        color: #004e85;
        border: 1px solid #c4ccd5;
        border-top: 10px solid rgba(0, 91, 154, 0.58);
        /*border-radius: 3px;*/
        font-weight: bold;
        font-size: 24px;
        display: flex;
        justify-content: center;
        align-items: center;

        img {
            width: 25px;
            height: 25px;
            margin-right: 20px;
        }
    }

        .add-btn{
            height:40px !important;
            width:186px !important;
            position:absolute;

            border:2px solid rgba(218,218,218,0.62);
            border-radius: 5px;

    }
    .el-notification {
        white-space:pre-wrap !important;
    }




</style>
