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

        <el-row class="win" type="flex" justify="center">
            <el-col :span="6">
                <el-row type="flex" justify="space-around" style="background-color: #e4e9f3">
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

                    <el-button type="primary" icon="el-icon-plus"  plain style="float:right;margin:10px 30px" @click="goto('propreg')">New</el-button>
                    </el-col>
                </el-row>
                <el-row class="property-list">
                <el-card
                        class="card2"
                        :class="addStatusColor(item.status)"
                        v-for="item in propList"
                        @click.native="selectItem(item)"
                        :key="item.pid"
                        :style="{'background-color' : item.pid === isSelected ? 'rgba(154,174,195,0.45)' : ''}"
                >
                    <el-row>
                        <div>
                            <el-row>
                                <el-col :span="20">
                            <h6>{{ decapitateAddress(item.address) }}</h6>
                                </el-col>

                                <el-col :span="1" >
                                <el-tooltip v-show="item.status === 'R'" class="item" effect="dark" content="Cancel" placement="right">
                                <el-button v-show="item.status === 'R'" type="" plain circle="" icon="el-icon-close" @click="cancelAuc(item)"></el-button>
                                </el-tooltip>

                                <el-tooltip v-show="item.status === 'N'" class="item" effect="dark" content="Remove" placement="right">
                                <el-button style="margin-left:0" v-show="item.status === 'N'" type="" plain circle icon="el-icon-close" @click="removeItem(item.pid)"></el-button>
                                </el-tooltip>
                                </el-col>
                            </el-row>
                            <p>{{ getlabel(item.status) }}</p>
                        </div>
                        <el-row type="flex" justify="end">
                            <el-button class="btn-long" v-show="item.status === 'R'" type="success"  round icon="el-icon-right" @click="goDetails(item)">Details</el-button>
                            <el-button v-show="item.status === 'N'" type="info"  round icon="el-icon-document" @click="aucreg">Register</el-button>
<!--                            <el-button v-show="item.status === 'N'" type="" plain round icon="el-icon-close" @click="removeItem(item.pid)">Remove</el-button>-->
<!--                            <el-button v-show="item.status === 'R'" type="" plain round icon="el-icon-close" @click="cancelAuc(item)">Cancel</el-button>-->
                            <el-button v-show="item.status === 'A'" type="success"  round icon="el-icon-right" @click="goDetails(item)">Details</el-button>
                        </el-row>
                    </el-row>
                </el-card>
                </el-row>
            </el-col>


            <el-col :span="18" style="padding: 0 50px">
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
                    <el-row v-show='!this.isEmpty' class="property-item" style="margin-top:50px">
                        <section>
                            <h3>{{ propInfo.address }}</h3>
                    <el-carousel :interval="5000" arrow="always" :width="cwidth" :height="cheight" style="margin: 0 15% 5% 0">
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
                        <h5>Description</h5>
                    </el-row>

                    <el-row style="margin-bottom: 10px;">
                        <p style="word-wrap:break-word">{{ propInfo.description }}</p>
                    </el-row>


                    <el-row type="flex" style="align-items:center">
                        <h5>Keywords</h5>
                        <!--                        <el-button>Edit</el-button>-->
                    </el-row>

                    <el-row type="flex" style="margin-bottom: 10px;">
                        <!--                        <el-tag class='tag1' v-for="tag in propInfo.position.split(',')" effect="plain" :key="tag.id">{{ tag }}</el-tag>-->
                        <p class='tag-wrap' v-for="tag in (propInfo.position||'').split(',')" >{{ tag }}</p>
                        <p class='tag-wrap3' v-for="tag in (propInfo.detail||'').split(',')" >{{ tag }}</p>
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
                            >
                                <el-row :gutter="50">
                                    <el-col :span="24">
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
                                            <el-input v-model="form.price">
                                                <i slot="suffix" class="input-slot">{{form.price|numFormat}} A$</i>
                                            </el-input>
                                        </el-form-item>

                                        <el-form-item label="Minimum Price:" prop="minimumPrice">
                                            <el-input v-model="form.minimumPrice">
                                                <i slot="suffix" class="input-slot">{{form.minimumPrice|numFormat}} A$</i>
                                            </el-input>
                                        </el-form-item>


                                    </el-col>
                                </el-row>
                            </el-form>
                                <div slot="footer" class="dialog-footer">
<!--                                    <el-button @click="backAuc">Back</el-button>-->
                                    <el-button type="primary" @click="submitReg(propInfo.pid)">Submit</el-button>
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
            return {
                unread:'',
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
                    //     address:'afdgdag',
                    //     position:'apple,pear',
                    //     detail:'bbq,ppol',
                    //     photos:['', ''],
                    //     bathroomNum:2,
                    //     bedroomNum:1,
                    //     garageNum:2,
                    //
                    // }
                    // ,{
                    //     pid:2,
                    //     status: 'N',
                    //     address:'123asd1231231231231231321231231312313',
                    //     position:'apple,pear',
                    //     detail:'bbq,ppol',
                    //     photos:['','']
                    // },
                    // {
                    //     pid:3,
                    //     aid:1,
                    //     auction:true,
                    //     position:'apple,pear',
                    //     detail:'bbq,ppol',
                    //     status: 'A',
                    //     address:'123asd',
                    //     photos:['','']
                    // }
                ],
                propList: [],
                propInfo: {
                    position:'',
                    detail:''
                },
                rules: {
                },

                form:{
                    daterange:'',
                    price:'',
                    minimumPrice:'',
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
                            }
                        } else if(response.data.code === 400){
                            this.$message.error(response.msg);
                        }else{
                            console.log(response.msg);
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
                                        // this.propInfo.auction = true;
                                        this.Aucreg = false;
                                        location.reload()
                                    }
                                } else if(response.data.code === 400){
                                    this.Aucreg = false;
                                    this.$message.error(response.msg);
                                }else{
                                    console.log(response.msg);
                                }
                            })
                            .catch((res) => {
                                console.log('error', res);
                                this.$message.error('New Auction Register Error');
                            });
                    } else {
                        return false;
                    }
                });
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
                this.Aucreg = true;
            },

            showdate(t){
                return dayjs(t).format("YYYY-MM-DD HH:mm:ss")
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
                                    }else{
                                        console.log(response.msg);
                                        location.reload()
                                    }
                                }else{
                                    console.log(response.msg)
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
        margin: 20px 20px;
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
        border-left: 15px solid #e7b993;
    }
    .status-process {
        border-left: 15px solid #aed991;
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


</style>
