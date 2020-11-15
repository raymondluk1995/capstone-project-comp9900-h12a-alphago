<template>
    <div class="propmag">
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

        <el-row class="win" type="flex" justify="center" style="box-shadow: inset 0px 15px 20px -15px rgba(70,92,132,0.45);">
            <el-col :span="6">
                <el-row type="flex" justify="space-around" style="background-color: rgba(38,66,123,0.8)">
                    <el-col>
                    <el-select
                            v-model="filter"
                            style="margin:10px 0 10px 30px"
                            placeholder="Select"
                            @change="changeSearch"
                    >
                        <el-option
                                v-for="item in options"
                                :key="item.value"
                                :label="item.label"
                                :value="item.value"
                        >
                        </el-option>
                    </el-select>
                    </el-col>
                    <el-col>

                    <el-button type="primary" icon="el-icon-plus"  plain style="float:right; margin:10px 30px" @click="goto('propreg')">New</el-button>
                    </el-col>
                </el-row>
                <el-row class="property-list">
                 <div  v-for="item in propList"  :key="item.pid" :class="addStatusColor(item.status)">
                <el-card
                        class="card2"
                        :style="{'background-color' : item.pid === isSelected ? 'rgba(154,174,195,0.45)' : ''}"
                        @click.native="selectItem(item)"
                >
                    <el-row>
                        <div >
                            <el-row >
                                <el-col :span="20">
                            <h6>{{ decapitateAddress(item.address) }}</h6>
                                </el-col>

                            </el-row>
                           <el-row>
                               <el-col :span="17">
                                     <p>{{ getlabel(item.status) }}</p>
                                </el-col>

                               <el-col :span="3" >
                                   <el-button v-show="item.status === 'R'" type="" plain round icon="el-icon-close" @click="cancelAuc(item)">Cancel</el-button>
                                   <el-button style="margin-left:0" v-show="item.status === 'N'" type="" plain round icon="el-icon-close" @click="removeItem(item.pid)">Remove</el-button>
                               </el-col>
                           </el-row>

                        </div>

                        <el-row type="flex" justify="right">
                            <el-col :span="17">
                            </el-col>
                            <el-col :span="3" >
                            <el-button v-show="item.status === 'R'" type="success"  round icon="el-icon-right" @click="goDetails(item)">Details</el-button>
                            <el-button v-show="item.status === 'N'" type="info"  style="margin-left:0"  round icon="el-icon-document" @click="aucreg">Register</el-button>
<!--                            <el-button v-show="item.status === 'N'" type="" plain round icon="el-icon-close" @click="removeItem(item.pid)">Remove</el-button>-->
<!--                            <el-button v-show="item.status === 'R'" type="" plain round icon="el-icon-close" @click="cancelAuc(item)">Cancel</el-button>-->
                            <el-button v-show="item.status === 'A'" type="success" style="margin-left:0"  round icon="el-icon-right" @click="goDetails(item)">Details</el-button>
                            </el-col>
                        </el-row>
                    </el-row>
                </el-card>
                 </div>
                </el-row>

            </el-col>


            <el-col :span="18" style="padding: 0 50px;">
                <div v-show="this.isEmpty">
                    <el-alert
                            title="You haven't register any property!"
                            type="info"
                            center
                            show-icon
                            :closable="false">
                    </el-alert>
                    <!--                        <el-button type="primary" icon="el-icon-right" round plain @click="goto('propreg')">Register New Auction</el-button>-->
                </div>
                    <el-row v-show='!this.isEmpty' class="property-item" style="margin-top:50px;">
                        <section>
                            <h3 style="font-size: 20px;color:white;padding:20px;height:60px ;margin: 0 15% 0 0;background-color:  rgba(38,66,123,0.8)">
                                <i class="el-icon-location"></i>
                                {{ propInfo.address }}</h3>
                    <el-carousel :interval="5000" arrow="always" :width="cwidth" :height="cheight" style="margin: 0 15% 3% 0">
                        <el-carousel-item v-for="pic in propInfo.photos" :key="pic">
                            <img :src="pic"  width="100%" height="100%" alt=""/>
                        </el-carousel-item>
                    </el-carousel>

                </section>


                <section>
                    <el-row type="flex" justify="left" style="margin:10px 0;">
                        <el-col :span="4">
                            <i class="fas fa-bath" style="margin-right: 5px"></i> Baths: {{ propInfo.bathroomNum}}
                        </el-col>
                        <el-col :span="4">
                            <i class="fas fa-bed" style="margin-right: 5px"></i> Beds: {{ propInfo.bedroomNum }}
                        </el-col>
                        <el-col :span="4">
                            <i class="fas fa-car" style="margin-right: 5px"></i> Cars: {{ propInfo.garageNum }}
                        </el-col>
                        <el-col :span="4">
                            <i class="fas fa-home" style="margin-right: 5px"></i> Area: {{ propInfo.area }}
                        </el-col>
                        <el-col :span="8">
                            <i class="fas fa-tags" style="margin-right: 5px"></i> Type: {{ propInfo.type }}
                        </el-col>
                    </el-row>

                    <el-row type="flex" style="margin-bottom: 10px;margin-top:50px;">
                        <h5><i class="el-icon-tickets"></i> Description</h5>
                    </el-row>

                    <el-row style="margin-bottom: 10px;">
                        <p style="word-wrap:break-word">{{ propInfo.description }}</p>
                    </el-row>


                    <el-row type="flex" style="margin-bottom: 10px;align-items:center">
                        <h5><i class="el-icon-collection-tag"></i> Keywords</h5>
                        <!--                        <el-button>Edit</el-button>-->
                    </el-row>

                    <el-row class="items-tag" type="flex" style="margin-bottom: 10px;">
                        <!--                        <el-tag class='tag1' v-for="tag in propInfo.position.split(',')" effect="plain" :key="tag.id">{{ tag }}</el-tag>-->
<!--                        <p class='tag-wrap' v-for="tag in (propInfo.position||'').split(',')" >{{ tag }}</p>-->
                        <ul>
                            <li v-for="tag in ((propInfo.position||'')+(propInfo.detail !==''? (',' + propInfo.detail):'')).split(',')">
                                <p class='tag-wrap3' >{{ tag }}</p>
                            </li>
                        </ul>
                    </el-row>
                </section>


                        <el-dialog title="Register New Auction:" :visible.sync="Aucreg">
                            <el-form
                                    class="form"
                                    ref="form"
                                    :model="form"
                                    :rules="rules"
                                    label-width="150px"
                                    label-position="left"
                                    style="padding:0"
                            >
                                <el-row :gutter="50" type="flex" justify="center">
                                    <el-col :span="15">
                                        <el-form-item label="Time Range:" prop="daterange">
                                            <el-date-picker style="width:100%"
                                                            v-model="form.daterange"
                                                            type="datetimerange"
                                                            range-separator="-"
                                                            start-placeholder="Auction Start Time"
                                                            end-placeholder="Auction End Time"
                                                            value-format="timestamp"
                                                            :picker-options="pickerOptions">
                                            </el-date-picker>

                                        </el-form-item>

                                        <el-form-item label="Reserved Price:" prop="price">
                                            <el-input v-model="form.price"  maxlength="10">
                                                <i slot="suffix" class="input-slot">{{form.price|numFormat}} A$</i>
                                            </el-input>
                                        </el-form-item>

                                        <el-form-item label="Starting Price:" prop="minimumPrice">
                                            <el-input v-model="form.minimumPrice"  maxlength="10">
                                                <i slot="suffix" class="input-slot">{{form.minimumPrice|numFormat}} A$</i>
                                            </el-input>
                                        </el-form-item>


                                    </el-col>
                                </el-row>
                                <el-row style="border-top:2px solid lightslategrey;text-align:center">
                                    <p style="color:#b5b5b5;margin-top:20px">Select your payment card</p>
                                </el-row>

                                <el-row v-show="!addNewCard" type="flex" justify="center" style="margin-top:10px">
                                    <el-col :span="13">
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
                                                          @click="addnew"
                                                    >
                                                        <div style="margin:12px 0;text-align: center">
                                                        <i class="el-icon-plus">
                                                        </i> Add New Card
                                                        </div>

                                                    </span>

                                                </el-col>
                                            </div>
                                        </el-row>

                                    </el-col>
                                </el-row>

                                <el-row v-show="addNewCard" type="flex" justify="center">
                                    <el-col :span="15">

                                        <el-form
                                                class="form"
                                                ref="form2"
                                                :model="form2"
                                                :rules="rules"
                                                style="padding:0"
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
<!--                                            <el-row>-->
<!--                                                <div class="next-btn" style="margin-top:40px">-->
<!--                                                    <el-button type="primary" icon="el-icon-plus" round @click="submitcard">Submit New Card</el-button>-->
<!--                                                    <el-button type="success" icon="el-icon-left" round @click="backcard">Back</el-button>-->
<!--                                                </div>-->
<!--                                            </el-row>-->
                                        </el-form>
                                    </el-col>
                                </el-row>
                            </el-form>
                                <div  style="margin-top:10px;text-align: end" >
<!--                                    <el-button @click="backAuc">Back</el-button>-->
                                    <el-button v-show="addNewCard" type="primary" icon="el-icon-plus" @click="submitcard">Submit New Card</el-button>
                                    <el-button v-show="addNewCard" type="success" icon="el-icon-left" @click="backcard">Back</el-button>
                                    <el-button v-show="!addNewCard" type="primary" @click="submitReg(propInfo.pid)">Submit</el-button>
                                </div>
                        </el-dialog>



                        <!--                <el-row class="mh20" type="flex" style="align-items:center">-->
<!--                    <span class="mr20">Description </span>-->
<!--&lt;!&ndash;                    <el-button v-if="!canEditDesc" @click="editDesc">Edit</el-button>&ndash;&gt;-->
<!--&lt;!&ndash;                    <el-button v-else @click="saveDesc">Save</el-button>&ndash;&gt;-->
<!--                </el-row>-->

<!--                <el-card>-->
<!--                    <p>{{ propInfo.description }}</p>-->
<!--&lt;!&ndash;                    <el-input v-else type="textarea" v-model="desc"></el-input>&ndash;&gt;-->
<!--                </el-card>-->

<!--                <section class="mh20" v-if="propInfo.status ==='A' || propInfo.status ==='R'">-->
<!--                    <h5>Auction</h5>-->
<!--&lt;!&ndash;                    <el-button v-if="propInfo.status === 'R'" type="" plain round icon="el-icon-close" @click="cancelAuc(propInfo)">Cancel</el-button>&ndash;&gt;-->
<!--                    <p>Start Date: {{ showdate(propInfo.startDate) }}</p>-->
<!--                    <p>End Date: {{ showdate(propInfo.endDate) }}</p>-->
<!--                    <p>Reserved Price: ${{ propInfo.price }}</p>-->

<!--                </section>-->
<!--                <section class="mh20" v-else>-->
<!--                    <h5>Auction</h5>-->
<!--                    <p> This property has not been registered for an Auction. </p>-->

<!--                </section>-->
             </el-row>
            </el-col>
        </el-row>
    </div>
</template>

<script>
    import Header from "@/components/Header.vue";
    import { mapActions } from "vuex";
    import dayjs from "dayjs";
    import $ from 'jquery'


    export default {
        name: "Auction",
        components: {
            Header,
        },
        props: {
            cheight: {
                type: String,
                default: '500px'
            },
            cwidth:{
                type: String,
                default: '200px'
            }
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
                } else if(value > parseInt(this.form.price)) {
                    callback(new Error("Minimum price should smaller than reserved price!"));
                }
                else{
                    callback();
                }
            };
            return {
                addNewCard:false,
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
                    //     },
                    // {
                    //     paymentId:'16',
                    //     name:'Bob',
                    //     cardNumber:'1234123412341234',
                    //     cvc:'012',
                    // },
                    // {
                    //     paymentId:'17',
                    //     name:'Tom',
                    //     cardNumber:'4321432143214321',
                    //     cvc:'123',
                    // },
                    // {
                    //     paymentId:'16',
                    //     name:'Bob',
                    //     cardNumber:'1234123412341234',
                    //     cvc:'012',
                    // },
                    // {
                    //     paymentId:'17',
                    //     name:'Tom',
                    //     cardNumber:'4321432143214321',
                    //     cvc:'123',
                    // }
                ],
                unread:'',
                selectCard:'',
                Aucreg:false,
                active:'',
                isEmpty:false,
                hasLogin:false,

                filter: "all",
                isSelected: '',
                photos:[],
                canCancel :true,
                options: [
                    {
                        value: "all",
                        label: "All",
                    },
                    {
                        value: "N",
                        label: "Not register",
                    },
                    {
                        value: "R",
                        label: "Not start",
                    },
                    {
                        value: "A",
                        label: "In process",
                    },
                ],
                urlObjImg:{},
                originPropertyList: [
                    // {
                    //     pid:1,
                    //     status:'R',
                    //     address:'2 Gearin Alley, Mascot, NSW, 2020',
                    //     position:'Close to School,Close to Medical, Close to Train',
                    //     detail:'BBQ,swimming pool',
                    //     photos:['https://anywhere-live.s3.amazonaws.com/property_images/f7a6fd63-f66c-415b-b489-a8563120271d.jpg', ''],
                    //     bathroomNum:2,
                    //     bedroomNum:1,
                    //     garageNum:2,
                    //     type:'Apartment',
                    //     area:130,
                    //     description:'The coveted Ebony development has something special about it. From the stunning BBQ area ' +
                    //         'for all to enjoy or the communal veggie garden ready for you to create your own culinary masterpiece. ' +
                    //         'This building is simply elegant in its design, from the impressive entry way to the artwork on display. ' +
                    //         'This is a building you will be proud to say you are a part of.'
                    //
                    // },
                    // {
                    //     pid:2,
                    //     status: 'N',
                    //     address:'42 Rosebery Avenue, Rosebery, NSW, 2018',
                    //     position:'pear,apple,pear',
                    //     detail:'BBQ,swimming pool',
                    //     photos:['','']
                    // },
                    // {
                    //     pid:3,
                    //     aid:1,
                    //     address:'325/347 Camberwell Road, Camberwell, 3124',
                    //     auction:true,
                    //     position:'apple,pear',
                    //     detail:'BBQ,swimming pool',
                    //     status: 'A',
                    //     photos:['','']
                    // }
                ],
                propList: [],
                propInfo: {
                    position:'',
                    detail:''
                },
                rules: {
                    price: [{required: true, message: " Please enter price", trigger: "blur"}, {validator: checkInt,trigger: "blur" },],
                    minimumPrice: [{required: true, message: " Please enter start price", trigger: "blur"}, {validator: checkStart,trigger: "blur" },],
                },

                form:{
                    daterange:'',
                    price:'',
                    minimumPrice:'',
                },
                form2:{
                    name: '',
                    cardNumber: '',
                    expiredDate: '',
                    cvc: '',
                },
                pickerOptions: {
                    // disabledDate(time) {
                    //     return parseInt(time.getTime()) < Date.now()
                    // }
                },
            };
        },


        created(){
            this.username = localStorage.getItem("username");
            // this.username='123';
            // this.username = this.$store.state.username;

            if (this.username !== null) {
                this.hasLogin = true;
                this.avatar = localStorage.getItem("avatar");
                this.$axios
                    .get('/property/propties')
                    .then(response => {
                        if (response.data.code === 200) {
                            this.originPropertyList = response.data.result;
                            this.propList = response.data.result;
                            this.propInfo = this.originPropertyList[0];
                        }else if(response.data.code === 400){
                            this.isEmpty = true;
                        }
                    })
                    .catch(function (error) {
                        console.log(error);
                    })
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
            // this.isEmpty = false;
            // this.propList = this.originPropertyList;
            // this.propInfo = this.originPropertyList[0];
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
                                        this.$router.replace("/");
                                    }else if(response.data.code === 400){
                                        this.$message.error(response.data.msg);
                                    }else{
                                        console.log(response.data.msg);
                                    }
                                }else{
                                    console.log(response.data.msg)
                                }
                            })
                        break;
                    default:
                        break;
                }
            },

            decapitateAddress(addr){
                if (addr.length<30){
                    return addr;
                }
                else{
                    return (addr.slice(0,30)+"...");
                }
            },

            getlabel(item){
                const colors = new Map([
                    ["N", "Auction not register"],
                    ["A", "Auction in process"],
                    ["R", `Auction has not started`],
                ]);
                return colors.get(item);
            },
            showCard(card){
                var reg = /^(\d{4})\d+(\d{4})$/;
                return card.replace(reg, "$1 **** **** $2");
            },
            addnew(){
                this.addNewCard = true;
            },
            submitcard(){
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
                                if (response.data.code === 200){
                                    let newCard = response.data.result;
                                    this.cards.push({
                                        paymentId:newCard.paymentId,
                                        name:newCard.name,
                                        cardNumber:newCard.cardNumber,
                                        cvc:newCard.cvc,
                                        expiredDate:newCard.expiryDate});
                                }else if(response.data.code === 400){
                                    this.$message.error(response.data.msg);
                                }else{
                                    console.log(response.data.msg);
                                }
                            })
                            .catch(function (error) {
                                console.log(error)
                            });
                        this.form2.cvc = '';
                        this.form2.name = '';
                        this.form2.cardNumber = '';
                        this.form2.expiredDate = '';

                        this.addNewCard = false;
                    }
                    else{
                        this.$message.error("Please complete the form.");
                    }
                })
            },

            backcard(){
                this.form2.cvc = '';
                this.form2.name = '';
                this.form2.cardNumber = '';
                this.form2.expiredDate = '';
                this.addNewCard = false;
            },

            cancelAuc(item){
                this.$confirm('Cancel this Auction?', 'Alert', {
                    confirmButtonText: 'Confirm',
                    cancelButtonText: 'Cancel',
                    type: 'warning'
                }).then(() => {
                let data = new FormData();
                data.append('pid', item.pid);
                data.append('aid', item.aid);
                this.$axios.post('/property/cancel', data)
                    .then((response) => {
                        if (response.status >= 200 && response.status < 300) {
                            if(response.data.code === 200){
                                this.$message.success("Cancel successful!");
                                location.reload();
                            }else if(response.data.code === 400){
                                this.$message.error(response.data.msg);
                            }
                        }else{
                            console.log(response.data.msg);
                        }
                    })
                    .catch((res) => {
                        console.log('error', res);
                        this.$message.error('New Auction Register Error');
                    });
                })
            },
            goDetails (item) {
                this.$router.push(
                    {
                        path: '/auction',
                        query:
                            {
                                id: item.aid,
                            }
                    }
                )
            },
            submitReg(pid){
                if(this.selectCard!==''){
                    this.$refs["form"].validate((valid) => {
                        if (valid) {
                            let data = new FormData();
                            data.append('pid', pid);
                            data.append('startdate', this.form.daterange[0]);
                            data.append('enddate', this.form.daterange[1]);
                            data.append('price', this.form.price);
                            data.append('minimumPrice', this.form.minimumPrice);

                            this.$axios.post('/property/newAuction', data)
                                .then((response) => {
                                    if (response.status >= 200 && response.status < 300) {
                                        if(response.data.code === 200){
                                            this.$message.success("Register successful!");
                                            this.Aucreg = false;
                                            location.reload()
                                        }else if(response.data.code === 400){
                                            this.Aucreg = false;
                                            this.$message.error(response.data.msg);
                                        }
                                    }else{
                                        console.log(response.data.msg);
                                    }
                                })
                                .catch((response) => {
                                    console.log('error', response);
                                    this.$message.error('New Auction Register Error');
                                });
                        } else {
                            return false;
                        }
                    });
                }
                else{
                    this.$message.error('Please select or add a card!');
                }
            },

            addStatusColor(status) {
                const colors = new Map([
                    ["N", "status-not-register"],
                    ["A", "status-process"],
                    ["R", "status-not-start"],
                ]);
                return colors.get(status);
            },

            aucreg(){
                this.form.minimumPrice = '';
                this.form.price = '';
                this.form.daterange = '';
                this.form2.cvc = '';
                this.form2.name = '';
                this.form2.cardNumber = '';
                this.form2.expiredDate = '';
                this.addNewCard = false;
                this.Aucreg = true;
                this.$axios
                    .get('/payment/get/')
                    .then(response => {
                        this.cards = response.data.result;
                        if(this.cards.length !== 0){
                            this.selectCard = this.cards[0].paymentId;
                        }
                    })
                    .catch(function (error) {
                        console.log(error)
                    });

            },

            changeSearch(value) {
                let filterPropertyList = [];
                if(value === 'all'){
                    filterPropertyList = this.originPropertyList;
                }else{
                    filterPropertyList = this.originPropertyList.filter((e) => {
                        return e.status === value;
                    });
                }
                this.propList = filterPropertyList;
            },

            selectItem(item) {
                this.propInfo = item;
                this.isSelected = item.pid;
            },

            removeItem(pid) {
                    this.$confirm('Remove this property?', 'Alert', {
                        confirmButtonText: 'Confirm',
                        cancelButtonText: 'Cancel',
                        type: 'warning'
                    }).then(() => {
                        let data = new FormData();
                        data.append('pid', pid);
                        this.$axios.post('/property/delete', data)
                            .then((response) => {
                                if (response.status >= 200 && response.status < 300){
                                    if (response.data.code === 200){
                                        this.$message({
                                            type: 'success',
                                            message: 'Remove!'
                                        });
                                        location.reload();
                                    }
                                    else if(response.data.code === 400){
                                        this.$message.error(response.data.msg);
                                    }else{
                                        console.log(response.data.msg);
                                        location.reload()
                                    }
                                }else{
                                    console.log(response.data.msg)
                                }
                            })
                    })
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
            ['form.price'](val) {
                this.$nextTick(() => {
                    this.form.price = val.replace(/\D/g,'');
                });
            },
            ['form.minimumPrice'](val) {
                this.$nextTick(() => {
                    this.form.minimumPrice = val.replace(/\D/g,'');
                });
            },
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
        }
    };
</script>

<style lang="scss">
    @import url("https://fonts.googleapis.com/css2?family=Dancing+Script:wght@700&display=swap");

    .mh20 {
        margin: 20px 0;
    }
    .mr20 {
        margin-right: 20px;
    }
    .user {
        display: flex;
        align-items: center;
        p {
            margin-left: 10px;
        }
    }
    .win {
        /*margin-top:10px;*/
        height: calc(100vh - 200px);
        /*margin: 0 20px;*/

    }
    .property-list{
        /*margin-top: 40px;*/
        height: calc(100vh - 260px);
        padding: 10px;
        overflow-y: scroll;
        background-color: #f1f1f1;
        box-shadow: inset 2px 1px 5px 4px #d5dbea;
    }
    .property-item {
        height: calc(100vh - 300px);
        /*margin-top: 20px;*/
        padding: 10px;
        overflow-y: scroll;
        margin-right:50px;
        margin-left:50px;
        /*box-shadow: 2px 1px 5px 4px #d5dbea;*/
    }
    .card2 {
        margin: 20px 0;
        &:hover {
            cursor: pointer;
            background-color: #d6e2e7;
            color: #f00;
        }
        h3,
        p {
            line-height: 2.5;
        }
    }
    .status-not-start {
        border-left: 15px solid #e77d6d;
        /*border : 10px solid black;*/
    }
    .status-process {
        border-left: 15px solid rgba(102, 199, 91, 0.92);
    }

    .status-not-register {
        border-left: 15px solid #8a97a6;
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
    .items-tag li{
        display:inline-block;
    }

    .items-tag ul li {
        position: relative;
        list-style-type:none;
    }


</style>
