<template>
    <div class="auction">
        <Header>
            <template v-if="this.hasLogin">
                <el-dropdown trigger="click" @command="handleCommand" style="align-items: center" placement="bottom">
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

        <el-row type="flex" justify="center">
            <el-col :span="24">
                <template v-if="!isEmpty">
                    <el-row type="flex" justify="space-around" style="background-color: #3b4c73;box-shadow: 0 2px 5px 2px #d5dbea;">
                        <el-col>
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
                    </el-row>

                    <el-row type="flex" justify="space-around">
                    <div class="items">
                        <ul>
                            <li  v-for="item in propList" :key="item.pid ">
                                <div class="item" @click.native="goDetails(item)">
                                    <el-carousel :interval="5000" arrow="always" :height="cheight" style="margin: 0 2%">
                                        <el-carousel-item v-for="pic in item.photos" :key="pic.id">
                                            <img :src="pic"  width="100%" height="100%" alt=""/>
                                        </el-carousel-item>
                                    </el-carousel>
                                    <div style="padding: 10px;">
                                    <h4>{{ item.address }}</h4>
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
                                            <div class="bid" v-if="item.status==='R'"> Guide ${{ getBidStatus(item)|numFormat }}</div>
                                            <div class="bid" v-else> ${{ getBidStatus(item)|numFormat }}</div>
                                            <template v-if="item.status==='A'">
                                            <div v-if="item.userBidPrice !==''" class="bid" :class="userBid(item.latestPrice, item.userBidPrice)"> ${{ getStatus(item)|numFormat }}</div>
                                            <div v-else class="bid" :class="userBid(item.latestPrice, item.userBidPrice)">$0</div>

                                            </template>
                                            <template v-else>
                                            <div class="bid" :class="addStatusColor(item.status)"> {{ getStatus(item) }}</div>
                                            </template>
                                        </el-row>
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
    import numFormat from "../utils/numFormat";
    export default {
        name: "Auction",
        props: {
            cheight: {
                type: String,
                default: '350px'
            }
        },
        components: {
            Header,
        },
        created () {
            // this.username = localStorage.getItem('username');
            // if(this.username!==null){
            //     this.hasLogin = true;
            //     this.avatar = localStorage.getItem('avatar');
            //     this.firstname=  localStorage.getItem('firstname');
            // }else{
            //     this.$message.error("You should login first!");
            //     this.$router.push("/login");
            // }

            this.$axios
                .get('/property/information')
                .then(response => {
                    if (response.data.code === 200) {
                        this.isEmpty=false;
                        this.propList = response.data.result;
                    }
                    if (response.data.code === 401){
                        this.$message.error('You should login first!');
                        this.$router.replace("/login");
                    }
                })
                .catch(function (error) {
                    console.log(error)
                })

        },
        data() {
            return {
                filter:'all',
                isEmpty: false,
                hasLogin: false,
                filterPropertyList:[],
                originPropertyList:[
                    // {
                    //     pid:1,
                    //     status:'A',
                    //     bathroomNum:2,
                    //     bedroomNum:1,
                    //     garageNum:2,
                    //     type:'Apartment',
                    //     area:123,
                    //     address:'2 Gearin Alley, Mascot, NSW',
                    //     photos:['https://t8.baidu.com/it/u=1484500186,1503043093&fm=79&app=86&size=h300&n=0&g=4n&f=jpeg?sec=1604152614&t=77f10b8e9e3d0e354e35cbbea5fe129a', ''],
                    //     latestPrice: 123123,
                    //     userBidPrice:123124,
                    //     reservedPrice: 10000,
                    // }
                    // ,{
                    //     pid:2,
                    //     status: 'A',
                    //     address:'123asd',
                    //     photos:['',''],
                    //     latestPrice: 123123,
                    //     userBidPrice:123,
                    //     reservedPrice: 10000,
                    // }
                    // ,{
                    //     pid:7,
                    //     status: 'S',
                    //     address:'123asd',
                    //     photos:['',''],
                    //     latestPrice: 123123,
                    //     userBidPrice:22222,
                    //     reservedPrice: 10000,
                    // },
                    // {
                    //     pid:3,
                    //     auction:true,
                    //     status: 'F',
                    //     address:'123asd',
                    //     photos:['',''],
                    //     latestPrice: 123123,
                    //     userBidPrice:22222,
                    // },
                    // {
                    //     pid:4,
                    //     auction:true,
                    //     status: 'F',
                    //     address:'123asd',
                    //     photos:['',''],
                    //     latestPrice: 123123,
                    //     userBidPrice:22222,
                    // },
                    // {
                    //     pid:5,
                    //     auction:true,
                    //     status: 'A',
                    //     address:'123asd',
                    //     photos:['',''],
                    //     latestPrice: 123123,
                    //     userBidPrice:22222,
                    // },
                    // {
                    //     pid:6,
                    //     auction:true,
                    //     status: 'A',
                    //     address:'123asd',
                    //     photos:['',''],
                    //     latestPrice: 123123,
                    //     userBidPrice:22222,
                    // },
                    // {
                    //     pid:8,
                    //     auction:true,
                    //     status: 'A',
                    //     address:'123asd',
                    //     photos:['',''],
                    //     latestPrice: 123123,
                    //     userBidPrice:22222,
                    // },
                    // {
                    //     pid:9,
                    //     auction:true,
                    //     status: 'A',
                    //     address:'123asd',
                    //     photos:['',''],
                    //     latestPrice: 123123,
                    //     userBidPrice:22222,
                    // }

                ],
                propList:[],
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
                    {
                        value: "S",
                        label: "Success",
                    },
                    {
                        value: "F",
                        label: "Failure",
                    },

                ],
                propList: [{
                    status:'',
                    address:'sdf13',
                    bathroomNum:1,
                    bedroomNum:2,
                    garageNum:2,
                    photos:['',''],
                }],
            }
        },
        created(){
            // this.username = localStorage.getItem("username");
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

            this.$axios
                .get('/auction')
                .then(response => {
                    if (response.data.code === 200) {
                        this.originPropertyList = response.data.result;
                        this.propList = response.data.result;
                        // this.propInfo = this.originPropertyList[0];
                    }else if(response.data.code === 400){
                        this.isEmpty = true;
                    }
                })
                .catch(function (error) {
                    console.log(error);
                })
            // this.isEmpty = false;
            // this.propList = this.originPropertyList;
            // this.propInfo = this.originPropertyList[0]
            ;
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
                    case "property":
                        this.$router.push("/propmag");
                        break;
                    case "notification":
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
                        return 'Waiting';
                        break;
                    case 'A':
                        return `${ item.userBidPrice }`;
                        break;
                    case 'S':
                        return 'Success';
                        break;
                    case 'F':
                        return 'Failed';
                        break;
                    default:
                        break;
                }
            },
            getBidStatus(item){
                if(item.status==='R'){
                    return item.reservedPrice;
                }else{
                    return item.latestPrice;
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
                }else{
                    return 'user-bid-bg-above';
                }
            },
            addStatusColor(status) {
                const colors = new Map([
                    ["R", "status-not-start"],
                    ["A", "status-process"],
                    ["S", "status-success"],
                    ["F", "status-failure"],
                ]);
                return colors.get(status);
            },

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
                        path: '/property',
                        query:
                            {
                                id: item.pid,
                            }
                    }
                )
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
.card {
    margin: 20px 25%;
    height: 600px;

    &:hover {
        cursor: pointer;
        transform: scale(1.02);
    }

    h6{
        margin:5px 5%;
    }
    p {
        line-height: 2.5;
    }
}
.empty-label{
    /*font-size: 18px;*/
    text-align: center;
    margin: 10px;
}
.bid {
    width :45%;
    padding: 5px;
    text-align: center;
    font-size: 15px;
    font-weight: bold;
    color: #fff;
    background-color: #133264;
    border-radius: 5px;
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
    background: #ca4b2e;
}
.user-bid-bg-above{
    background-color: #0F996B;
}


.items{
    margin: 0 10%;
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
    height: 500px;
    padding: 10px 0;
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

.status-failure {
    background-color: rgba(50, 17, 18, 0.67);
}

.status-process {
    background-color:  #d7d997;
}

.status-success {
    background-color: rgba(135, 196, 116, 0.82);
}

.status-not-start {
    /*border-top: 15px solid #bcc8e6;*/
    background-color:  #8a97a6;
}



</style>
