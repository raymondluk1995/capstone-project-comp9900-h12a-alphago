<template>
    <div class="auction">
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

        <el-row type="flex" justify="center">
            <el-col :span="24" style="background: white;">
                    <el-row type="flex" justify="space-around" style="background-color: #3b4c73;box-shadow: 0 2px 5px 2px #d5dbea;">
                        <el-col :span="4">
                            <el-select
                                    v-model="filter"
                                    style="margin:10px 17%; width:200px"
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
                        <el-col :span="9" >
                            <div class="reg-btn" style="float:right; margin-top:15px">
                                <span  id="reg-btn"
                                       style="
                                           color:white;
                                           padding:2px 5px;
                                           font-size:20px;
                                            position:absolute;
                                            "
                                       @click="goto('auchis')">
                                    <i class="el-icon-right"></i> View History</span>
                            </div>
                        </el-col>
                    </el-row>

                <template v-if="!isEmpty">
                    <el-row type="flex" justify="space-around">
                    <div class="items">
                        <ul>
                            <li  v-for="item in propList" :key="item.aid " @click="goDetails(item)">
                                <div class="item">
                                    <el-row  v-if="item.status === 'R'"
                                             :class="addStatusColor(item)"
                                             style="height:60px;padding:0 10px;">
                                        <div style="color:#2e2a10;float:left;font-weight: bold;margin-top:15px;">
                                            <span style="font-size:15px;">Auction:</span>
                                            <span style="font-size: 20px">{{ showTime(item.startdate)}}</span>
<!--                                            <h5 >Auction: {{ showTime(item.startdate) }}</h5>-->
                                        </div>
                                        <div style="float:right;font-weight: bold;padding:15px;">
                                            <span style="font-size:15px">Guide Bid  </span>
                                            <span style="font-size:20px">${{item.currentBid|numFormat}}</span>
                                            <span style="font-size:15px">AUD</span>
                                        </div>


                                    </el-row>

                                    <el-row v-else
                                            :class="addStatusColor(item)"
                                            style="height:60px;padding:0 10px;">
                                        <div style="float:left;font-weight: bold;margin-top:15px;">
                                        <span style="font-size:15px;">End at:</span>
                                        <span style="font-size: 20px">{{ showTime(item.enddate)}}</span>
                                        </div>
                                        <div style="float:right;font-weight: bold;padding:15px;">
                                            <span style="font-size:15px">Latest Bid  </span>
                                            <span style="font-size:20px">${{item.currentBid|numFormat}}</span>
                                            <span style="font-size:15px">AUD</span>
                                        </div>
                                    </el-row>


                                    <el-carousel :interval="5000" arrow="always" :height="cheight" >
                                        <el-carousel-item v-for="pic in item.photos" :key="pic.id">
                                            <img :src="pic"  width="100%" height="100%" alt=""/>
                                        </el-carousel-item>
                                    </el-carousel>
                                    <div style="padding: 20px;">
                                    <h5>{{ item.address }}</h5>
                                    <el-row type="flex" justify="left" style="margin:10px 5%;">
                                        <el-col :span="4">
                                        <i class="el-icon-toilet-paper"> Baths: {{ item.bathroomNum}}</i>
                                        </el-col>
                                        <el-col :span="4">
                                        <i class="el-icon-house"> Beds: {{ item.bedroomNum }}</i>
                                        </el-col>
                                            <el-col :span="4">
                                        <i class="el-icon-truck"> Cars: {{ item.garageNum }}</i>
                                            </el-col>
                                        <el-col :span="4">
                                            <i class="el-icon-full-screen"> Area: {{ item.area }}</i>
                                        </el-col>
                                        <el-col :span="8">
                                            <i class="el-icon-info"> Type: {{ item.type }}</i>
                                        </el-col>
                                    </el-row>
                                        <el-row type="flex" justify="space-around">
<!--                                            <div class="bid" v-if="item.status==='R'"> Guide ${{ getBidStatus(item)|numFormat }}</div>-->
<!--                                            <div class="bid" v-else> ${{ getBidStatus(item)|numFormat }}</div>-->

                                            <div v-if="parseInt(item.currentBid) === parseInt(item.highestPrice)"
                                                 class="bid"
                                                 :class="userBid(item.highestPrice, item.currentBid)"> Winning! </div>
                                            <div v-else
                                                 class="bid"
                                                 :class="item.status==='R'? 'banner-grey' : userBid(item.highestPrice, item.currentBid)">
                                                <span v-if="item.status==='A'" style="font-size:15px">My Bid </span>
                                                <span v-else style="font-size:15px">My Initial Bid </span>
                                                ${{ item.highestPrice |numFormat }} </div>

                                        </el-row>
<!--                                        <el-row type="flex" justify="space-around">-->
<!--                                            <template v-if="item.status==='A'">-->
<!--                                                <div v-if="parseInt(item.currentBid) === parseInt(item.highestPrice)"-->
<!--                                                     class="bid2"-->
<!--                                                     :class="userBid(item.highestPrice, item.currentBid)"> Winning! </div>-->
<!--                                                <div v-else-->
<!--                                                     class="bid2"-->
<!--                                                     :class="userBid(item.highestPrice, item.currentBid)"> ${{ getStatus(item) |numFormat }} </div>-->

<!--                                            </template>-->
<!--                                            <template v-else>-->
<!--                                                <div class="bid2" :class="addStatusColor(item.status)"> {{ getStatus(item) }}</div>-->
<!--                                            </template>-->
<!--                                        </el-row>-->
                                </div>
                                </div>
                            </li>
                        </ul>
                    </div>
                    </el-row>

                </template>
                <template v-else>
                    <div class="empty-label" >
                        <el-alert
                                title="You do not have any auction."
                                type="info"
                                center
                                show-icon
                                :closable="false">
                        </el-alert>
                    </div>
                </template>


            </el-col>
        </el-row>
    </div>
</template>

<script>
    import Header from "@/components/Header.vue";
    import { mapActions } from "vuex";
    import dayjs from "dayjs";
    import $ from 'jquery';
    import numFormat from "../utils/numFormat";

    export default {
        name: "Auction",
        props: {
            cheight: {
                type: String,
                default: '320px'
            }
        },
        components: {
            Header,
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
            return {
                id:'',
                unread:'',

                filter:'all',
                isEmpty: false,
                hasLogin: false,
                filterPropertyList:[],
                originPropertyList:[
                    // {
                    //     aid:1,
                    //     status:'A',
                    //     bathroomNum:2,
                    //     bedroomNum:1,
                    //     garageNum:2,
                    //     type:'Apartment',
                    //     area:123,
                    //     address:'2 Gearin Alley, Mascot, NSW',
                    //     photos:['https://t8.baidu.com/it/u=1484500186,1503043093&fm=79&app=86&size=h300&n=0&g=4n&f=jpeg?sec=1604152614&t=77f10b8e9e3d0e354e35cbbea5fe129a', ''],
                    //     highestPrice: 123123,
                    //     currentBid:123123,
                    //     startdate:'',
                    //     endddate:'',
                    // }
                    // ,{
                    //     aid:2,
                    //     status: 'A',
                    //     address:'123asd',
                    //     photos:['',''],
                    //     highestPrice: 123123,
                    //     startdate: new Date(2019,10,10,12,10),
                    //     currentBid:123,
                    //     reservedPrice: 10000,
                    // },
                    // {
                    //     aid:3,
                    //     status: 'R',
                    //     address:'123asd',
                    //     photos:['',''],
                    //     startdate: new Date(2021,10,10,12,10),
                    //     enddate: new Date(2023,10,10,10,10),
                    //     highestPrice: 123123,
                    //     currentBid:123,
                    //     reservedPrice: 10000,
                    // },
                    //
                    // {
                    //     aid:5,
                    //     status: 'A',
                    //     address:'123asd',
                    //     photos:['',''],
                    //     highestPrice: 123123,
                    //     currentBid:22222,
                    // },
                    // {
                    //     aid:6,
                    //     status: 'A',
                    //     address:'123asd',
                    //     photos:['',''],
                    //     highestPrice: 123123,
                    //     currentBid:22222,
                    // },
                    // {
                    //     aid:8,
                    //     status: 'A',
                    //     address:'123asd',
                    //     photos:['',''],
                    //     highestPrice: 123123,
                    //     currentBid:22222,
                    // },
                    // {
                    //     aid:9,
                    //     status: 'A',
                    //     address:'123asd',
                    //     photos:['',''],
                    //     highestPrice: 123123,
                    //     currentBid:22222,
                    // }

                ],
                options: [
                    {
                        value: "all",
                        label: "All",
                    },
                    {
                        value: "R",
                        label: "Not start",
                    },
                    {
                        value: "A",
                        label: "In process",
                    },
                    // {
                    //     value: "S",
                    //     label: "Sold",
                    // },
                    // {
                    //     value: "F",
                    //     label: "Passed In",
                    // },

                ],
                propList: [
                //     {
                //     status:'',
                //     address:'sdf13',
                //     bathroomNum:1,
                //     bedroomNum:2,
                //     garageNum:2,
                //     photos:['',''],
                // }
                ],
            }
        },
        created(){
            this.username = localStorage.getItem("username");

            if (this.username !== null) {
                this.hasLogin = true;
                this.avatar = localStorage.getItem("avatar");
                this.$axios
                    .get('/auction/list/now')
                    .then(response => {
                        if (response.data.code === 200) {
                            this.originPropertyList = response.data.result;
                            this.propList = response.data.result;
                            // this.propInfo = this.originPropertyList[0];
                            if(this.originPropertyList.length === 0){
                                this.isEmpty = true;
                                this.propList = this.originPropertyList;
                            }
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

            // this.isEmpty = true;
            // this.propList = this.originPropertyList;
        },

        methods: {
            ...mapActions(["logout"]),
            handleCommand(command) {
                switch (command) {
                    case "profile":
                        this.$router.push("/profile");
                        break;
                    case "auction":
                        this.$router.push("/auctionmag");
                        break;
                    case "property":
                        this.$router.push("/propmag");
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
            getStatus(item){
                switch(item.status){
                    case 'R':
                        let time = dayjs(item.startdate).format("YYYY-MM-DD HH:mm:ss")
                        return `This Auction will start at ${time}`;
                        break;
                    case 'A':
                        return  `${item.highestPrice}`;
                        break;
                    // case 'S':
                    //     return 'Success';
                    //     break;
                    // case 'F':
                    //     return 'Failed';
                    //     break;
                    default:
                        break;
                }
            },
            getBidStatus(item){
                if(item.status==='R'){
                    return item.currentBid;
                }else{
                    return item.highestPrice;
                }
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
            userBid(latest, user){
                let lp = parseInt(latest);
                let up = parseInt(user);
                if(user===''){
                    up = 0;
                }
                if(lp>up){
                    return 'user-bid-bg-under';
                }else if(lp === up){
                    return 'user-bid-winner'
                }
            },

            addStatusColor(item) {
                switch (item.status) {
                    case "A":
                        if(item.highestPrice === item.currentBid){
                            return "status-winner";
                        }else{
                            return "status-process1";
                        }

                        break;
                    case "R":
                        return "status-not-start1";
                        break;
                    default:
                        break;
                }
            },
            // addStatusColor(status) {
            //     const colors = new Map([
            //         ["R", "status-not-start1"],
            //         ["A", "status-process1"],
            //     ]);
            //     return colors.get(status);
            // },

            goto(name) {
                console.log(name);
                this.$router.push({ name: name });
            },
            back() {
                this.$router.go(-1);
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
            showTime(time){
                // let time = dayjs(day).format("YYYY-MM-DD HH:mm:ss");
                let MONTH =['Jan', 'Feb', 'Mar','Apr', 'May', 'Jun', 'Jul', 'Aug', 'Sep', 'Oct', 'Nov', 'Dec'];
                let day = dayjs(time).day();
                let mon = MONTH[dayjs(time).month()];
                let year = dayjs(time).year();
                let hour = dayjs(time).hour();
                let min = dayjs(time).minute();
                let formatTime = `${day} ${mon} ${year}, ${hour}:${min}`;
                return formatTime;
            },

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
.empty-label{
    /*font-size: 18px;*/
    text-align: center;
    margin: 10px;
}
.bid {
    width :90%;
    padding: 10px;
    text-align: center;
    font-size: 18px;
    font-weight: bold;
    color: #fff;
    background-color: #133264;
    border-radius: 3px;
    margin-top:15px;
}
.bid2 {
    width :90%;
    padding: 3px;
    text-align: center;
    font-size: 12px;
    font-weight: bold;
    color: #fff;
    margin-top:5px;
    background-color: #133264;
    border-radius: 3px;
}
.userBid{
    width :45%;
    padding: 5px;
    text-align: center;
    font-size: 15px;
    font-weight: bold;
    color: #fff;
    background-color: #133264;
    border-radius: 5px;
}
.img{
    width: auto;
    height: auto;
    max-width: 100%;
    max-height: 100%;
}

.user-bid-bg-under{
    /*background: rgba(206, 58, 49, 0.68);*/
    background-color: #133264;
}
.user-bid-winner{
    background: rgba(222, 213, 78, 0.76);
}
.user-bid-bg-above{
    background-color: rgba(16, 160, 112, 0.75);
}
.banner-grey{
    background-color: rgba(44, 49, 65, 0.56);
}

.items{
    margin: 10px 10%;
    text-align:center;

}
.items ul{
    text-align:center;
    /*display: inline-block;*/
}
.items li{
    /*text-align:center;*/
    /*float:left;*/
    display:inline-block;
}

.items ul li {
    /*z-index: 1;*/
    width: 600px;
    height: 550px;
    /*padding: 10px 0;*/
    margin: 10px 20px;
    /*display: inline-block;*/
    position: relative;
    border: 1px solid rgba(20, 52, 105, 0.55);
    list-style-type:none;
    &:hover {
        cursor: pointer;
        /*transform: scale(1.02);*/
        background-color: rgba(193, 212, 240, 0.55);
        transition-duration: 0.3s;
    }
}

.status-process1 {
    background-image: url("../assets/banner-bg-green.png");
}

.status-not-start1 {
    background-image: url("../assets/banner-bg-grey.png");
}
.status-winner {
    background-image: url("../assets/banner-bg-yelo.png");
}


.reg-btn{
    cursor: pointer;
    position:relative;
    width:150px;
    &:hover{
        transform:translateX(10px);
        transition-duration: 0.5s;
        border-bottom:1px solid white
    }
    float:left;
}

</style>
