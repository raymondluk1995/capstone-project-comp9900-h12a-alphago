<template>
    <div class="propmag">
        <Header>
            <template v-if="this.hasLogin">
                <el-dropdown trigger="click" @command="handleCommand">
                    <div class="user">
                        <el-avatar :size="70" :src="avatar"></el-avatar>
                    </div>
                    <el-dropdown-menu slot="dropdown">
                        <el-dropdown-item command="profile" icon="el-icon-user-solid"> My profile</el-dropdown-item>
                        <el-dropdown-item command="property"  icon="el-icon-house"> My Properties</el-dropdown-item>
                        <el-dropdown-item command="auction" icon="el-icon-s-home"> My Auctions</el-dropdown-item>
                        <el-dropdown-item command="notification"  icon="el-icon-bell"> Notifications</el-dropdown-item>
                        <el-dropdown-item command="logout" icon="el-icon-turn-off"> Log out</el-dropdown-item>
                    </el-dropdown-menu>
                </el-dropdown>
            </template>
            <template v-else>
                <el-button round @click="goto('login')">Sign In</el-button>
                <el-button round type="primary" @click="goto('register')">Sign Up</el-button>
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
                        class="card"
                        :class="addStatusColor(item.status)"
                        v-for="item in propList"
                        @click.native="selectItem(item)"
                        :key="item.pid"
                        :style="{'background-color' : item.pid === isSelected ? 'rgba(154,174,195,0.45)' : ''}"
                >
                    <el-row>
                        <div>
                            <h6>{{ item.address }}</h6>
                            <p>{{ getlabel(item.status) }}</p>
                        </div>
                        <el-row type="flex" justify="end">
                            <el-button  v-show="item.status === 'R'" type="success"  round icon="el-icon-right" @click="goDetails(item)">Details</el-button>
                            <el-button v-show="item.status === 'N'" type="info"  round icon="el-icon-document" @click="aucreg">Register</el-button>
                            <el-button v-show="item.status === 'N'" type="" plain round icon="el-icon-close" @click="removeItem(item)">Remove</el-button>
                            <el-button v-show="item.status === 'R'" type="" plain round icon="el-icon-close" @click="cancelAuc(item)">Cancel</el-button>
                            <el-button v-show="item.status === 'A'" type="success"  round icon="el-icon-right" @click="goDetails(item)">Details</el-button>
                        </el-row>
                    </el-row>
                </el-card>
                </el-row>
            </el-col>


            <el-col :span="18">
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
                    <el-carousel :interval="5000" arrow="always" :width="cwidth" :height="cheight" style="margin: 0 25% 0 25%">
                        <el-carousel-item v-for="pic in propInfo.photos" :key="pic">
                            <img :src="pic"  width="100%" height="100%" alt=""/>
                        </el-carousel-item>
                    </el-carousel>

                </section>

                <el-row type="flex" justify="end">
<!--                    <el-button>Edit</el-button>-->
                </el-row>

                <section>
                    <el-row class="mh20" type="flex">
                        <el-col :span="7">
                            <i class="el-icon-toilet-paper"> Bathroom Number: <span> {{ propInfo.bathroomNum}} </span></i>
                        </el-col>
                        <el-col :span="7">
                            <i class="el-icon-house"> Bedroom Number: <span> {{ propInfo.bedroomNum}} </span></i>
                        </el-col>
                        <el-col :span="7">
                            <i class="el-icon-truck"> Garage Number: <span> {{ propInfo.garageNum}} </span></i>
                        </el-col>
                    </el-row>
                    <el-row class="mh20" type="flex">
                        <el-col :span="7">
                            <i class="el-icon-info"> Type: {{ propInfo.type }}</i>
                        </el-col>
                        <el-col :span="7">
                            <i class="el-icon-full-screen"> Area: {{ propInfo.area }}</i>
                        </el-col>
                    </el-row>

                    <el-row type="flex" style="align-items:center">
                        <h5>Keywords</h5>
<!--                        <el-button>Edit</el-button>-->
                    </el-row>

                    <el-row type="flex" style="margin-bottom: 10px;">
                        <el-tag v-for="tag in (propInfo.position||'').split(',')" effect="plain" :key="tag">{{ tag }}</el-tag>
                    </el-row>

                    <el-row type="flex" style="margin-bottom: 10px;">
                        <el-tag v-for="tag in (propInfo.detail||'').split(',')" :key="tag">{{ tag }}</el-tag>
                    </el-row>

                    <el-row type="flex" style="margin-bottom: 10px;">
                        <h5>Description</h5>
                    </el-row>

                    <el-row style="margin-bottom: 10px;">
                        <p style="word-wrap:break-word">{{ propInfo.description }}</p>
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

                <section class="mh20" v-if="propInfo.status ==='A' || propInfo.status ==='R'">
                    <h5>Auction</h5>
<!--                    <el-button v-if="propInfo.status === 'R'" type="" plain round icon="el-icon-close" @click="cancelAuc(propInfo)">Cancel</el-button>-->
                    <p>Start Date: {{ showdate(propInfo.startDate) }}</p>
                    <p>End Date: {{ showdate(propInfo.endDate) }}</p>
                    <p>Reserved Price: ${{ propInfo.price }}</p>

                </section>
                <section class="mh20" v-else>
                    <h5>Auction</h5>
                    <p> This property has not been registered for an Auction. </p>
                    <el-row type="flex" justify="front" >
<!--                        <el-button type="primary" icon="el-icon-right" round  plain style="float:right;margin:10px 20px" @click="aucreg">Register New Auction</el-button>-->
                    </el-row>
                </section>
             </el-row>
            </el-col>
        </el-row>
    </div>
</template>

<script>
    import Header from "@/components/Header.vue";
    import { mapActions } from "vuex";
    import dayjs from "dayjs";


    export default {
        name: "Auction",
        components: {
            Header,
        },
        props: {
            cheight: {
                type: String,
                default: '300px'
            },
            cwidth:{
                type: String,
                default: '200px'
            }
        },

        data() {
            return {
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
                    {
                        pid:1,
                        status:'R',
                        address:'afdgdag',
                        photos:['', '']
                    }
                    ,{
                        pid:2,
                        status: 'N',
                        address:'123asd',
                        photos:['','']
                    },
                    {
                        pid:3,
                        aid:1,
                        auction:true,
                        status: 'A',
                        address:'123asd',
                        photos:['','']
                    }

                ],
                propList: [],
                propInfo: {},
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
            // // this.username = this.$store.state.username;
            // if (this.username !== null) {
            //     this.hasLogin = true;
            //     this.avatar = localStorage.getItem("avatar");
            //     this.firstname = localStorage.getItem("firstname");
            // }
            // else{
            //     this.$message.error("You should login first!");
            //     this.$router.push("/login");
            // }

            // this.$axios
            //     .get('/property/propties')
            //     .then(response => {
            //         if (response.data.code === 200) {
            //             this.originPropertyList = response.data.result;
            //             this.propList = response.data.result;
            //             this.propInfo = this.originPropertyList[0];
            //         }else if(response.data.code === 400){
            //             this.isEmpty = true;
            //         }
            //     })
            //     .catch(function (error) {
            //         console.log(error);
            //     })
            this.isEmpty = false;
            this.propList = this.originPropertyList;
            this.propInfo = this.originPropertyList[0];
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
                        this.$router.push("/notification");
                        break;
                    case "logout":
                        this.logout();
                        break;
                    default:
                        break;
                }
            },

            getlabel(item){
                const colors = new Map([
                    ["N", "Auction not register"],
                    ["A", "Auction in process"],
                    ["R", `Auction will start at ${item.startdate}`],
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

            removeItem(item) {
                    this.$confirm('Remove this property?', 'Alert', {
                        confirmButtonText: 'Confirm',
                        cancelButtonText: 'Cancel',
                        type: 'warning'
                    }).then(() => {
                        let data = new FormData();
                        data.append('pid', item.pid);
                        data.append('aid', item.aid);
                        this.$axios.post('/property/delete' + data)
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

<style scoped lang="scss">
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
    .card {
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

    .swiper-slide {
        height: 500px;
    }
</style>
