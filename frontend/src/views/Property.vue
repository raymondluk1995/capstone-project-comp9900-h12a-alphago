<template>
    <div class="property">
        <Header>
            <template v-if="this.hasLogin">
                <el-dropdown trigger="click" @command="handleCommand">
                    <div class="user">
                        <el-avatar :size="70" :src="avatar"></el-avatar>
                    </div>
                    <el-dropdown-menu slot="dropdown">
                        <el-dropdown-item command="profile" icon="el-icon-user-solid"> My profile</el-dropdown-item>
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
            <el-col :span="16">
                <section style="margin-top: 15px">
                    <h1>{{ propInfo.title }}</h1>
                    <el-carousel :interval="5000" arrow="always" :height="cheight">
                        <el-carousel-item v-for="pic in propInfo.photos" :key="propInfo.pid">
                            <img :src="pic"  width="100%" height="100%" alt=""/>
                        </el-carousel-item>
                    </el-carousel>
                </section>

                <el-row>
                    <el-col :span="12">
                        <h3>Current Bid</h3>
                        <div class="bid"> ${{ propInfo.currentBid}}</div>
                    </el-col>
                    <el-col :span="10" :offset="2">
                        <div v-if="hasBidder">
                            <h3>Place new bid</h3>
                            <div class="new-bid-wrap">
                                <el-input v-model="newBid" :disabled="timeFlag"></el-input>
                                <i class="el-icon-circle-plus-outline" @click="addNewBid"></i>
                            </div>
                        </div>

                        <div v-else style="text-align:right;margin-top: 70px">
                            <el-button type="primary"
                                       style="font-size: 20px;"
                                       round
                                       :disabled="timeFlag"
                                       @click="Bidreg"
                                      icon="el-icon-right"
                            >Register to Bid</el-button>
                        </div>
                    </el-col>
                </el-row>

                <el-dialog title="Register as a bidder" :visible.sync="bidderFlag">
                    <el-form
                            class="form"
                            ref="form"
                            :model="form"
                            :rules="rules"
                            label-width="120px"
                            label-position="left"
                    >
                        <el-form-item label="Name:" prop="name">
                            <el-input v-model="form.name"></el-input>
                        </el-form-item>
                        <el-form-item label="Card Number:" prop="cardNumber">
                            <el-input v-model="form.cardNumber"></el-input>
                        </el-form-item>
                        <el-form-item label="Expired Date:" prop="expiredDate">
                            <el-date-picker v-model="form.expiredDate" type="date">
                            </el-date-picker>
                        </el-form-item>
                        <el-form-item label="CVC:" prop="cvc">
                            <el-input v-model="form.cvc"></el-input>
                        </el-form-item>
                    </el-form>

                    <div slot="footer" class="dialog-footer">
                        <el-button @click="this.bidderFlag = false">Cancel</el-button>
                        <el-button type="primary" @click="submit">Submit</el-button>
                    </div>
                </el-dialog>

                <section>
                    <h3>Time left</h3>
                    <h2>{{ time }}</h2>
                </section>

                <section>
                    <el-row gutter="50">
                    <el-col :span="12">
                    <h3>Details</h3>
                    <el-row type="flex" style="margin-bottom: 10px;">
                            <i class="el-icon-toilet-paper"> Bathroom Number: <span> {{ propInfo.bathroomNum}} </span></i>
                    </el-row>

                    <el-row type="flex" style="margin-bottom: 10px;">
                            <i class="el-icon-house"> Bedroom Number: <span> {{ propInfo.bedroomNum}} </span></i>
                    </el-row>

                    <el-row type="flex" style="margin-bottom: 10px;">
                            <i class="el-icon-truck"> Garage Number: <span> {{ propInfo.garageNum}} </span></i>
                    </el-row>
                    <p>{{ propInfo.description }}</p>
                    </el-col>

                    <el-col :span="12">
                        <h3>Map</h3>
                        <div class="map">
                            <h3>...</h3>
                        </div>
                    </el-col>

                    </el-row>
                </section>
            </el-col>
        </el-row>
    </div>
</template>

<script>
    import Header from "@/components/Header.vue";
    import { mapActions } from "vuex";

    export default {
        name: "Home",
        props: {
            cheight: {
                type: String,
                default: '500px'
            }
        },
        components: {
            Header,
        },
        data() {
            return {
                endDate: new Date(2021, 10, 10, 10, 10),
                propInfo: {
                    id: 1,
                    title: "256 Gearin Alley,Mascot,Nsw",
                    info: "info",
                    currentBid:'123,000',
                    latestBid: "145,000",
                    newBid: "",
                    photos: [
                        '','',''
                        ],
                    description: 'This is a software project capstone course. Students work in teams of ideally five (5) members to define, implement ' +
                        'and evaluate a real-world software system. Most of the work in this course is team-based project work, although there are some ' +
                        'introductory lectures on software project management and teamwork strategies. Project teams meet weekly starting from Week 1 with' +
                        ' project mentors to report on the progress of the project. Assessment is based on a project proposal, two progressive demonstrations ' +
                        'and retrospectives, a final project demonstration and report, and on the quality of the software system itself. Students are also ' +
                        'required to reflect on their work and to provide peer assessment of their team-mates contributions to the project.',
                },
                hasBidder: false,
                bidderFlag: false,
                timeFlag: false,

                form: {
                    name: "",
                    cardNumber: "",
                    expiredDate: "",
                    cvc: "",
                },
                rules: {
                    name: [{required: true, message: " Please enter name", trigger: "blur",},],
                    cardNumber: [{required: true, message: " Please enter cardNumber", trigger: "blur",},],
                    expiredDate: [{required: true, message: " Please enter expired date", trigger: "blur",},],
                    cvc: [{required: true, message: " Please enter cvc", trigger: "blur",}],
                },
                newBid: "",
                tipError: false,
                time: "23:00:00",
            };
        },
        created() {
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
                .get('/property/information?pid=' + this.id)
                .then(response => {
                    this.form.propInfo = response.data.result
                })
                .catch(function (error) {
                    console.log(error)
                })


        },
        computed: {
        },

        mounted() {
            let timer = setInterval(() => {
                if (this.timeFlag === true) {
                    clearInterval(timer);
                }
            }, 1000);
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

            Bidreg() {
                this.bidderFlag = true;
            },

            submit() {
                this.$refs["form"].validate((valid) => {
                    if (valid) {
                        this.hasBidder = true;
                        this.bidderFlag = false;
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
    .bid {
        padding: 10px;
        text-align: center;
        font-size: 20px;
        font-weight: bold;
        color: #fff;
        background-color: #133264;
        border-radius: 5px;
    }

    .el-tag {
        margin-right: 20px;
    }
    .map {
        margin:10px;

        height:570px;
        width:570px;
        border: #4d5861 1px solid;
    }

</style>
