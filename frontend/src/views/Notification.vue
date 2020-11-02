<template>
    <div class="notification">
        <Header>
            <el-button round type="primary" @click="back">Back</el-button>
        </Header>

        <el-row type="flex" justify="center">
            <el-col :span="16">
                <el-collapse v-model="activeNames" @change="handleChange">
                    <el-collapse-item
                            v-for="(item, index) in NotificationList"
                            :key="index"
                            :title="item.title"
                            :name="index"
                    >
                        <el-card :class="item.read ? 'card' : 'card unread'">
                            <div @click="read(item)">{{ item.content }}</div>
                        </el-card>
                    </el-collapse-item>
                </el-collapse>
                <!-- <el-card
                  :class="item.read ? 'card' : 'card unread'"
                  v-for="(item, index) in NotificationList"
                  :key="index"
                >
                  <div @click="read(item)">{{ item.content }}</div>
                </el-card> -->
            </el-col>
        </el-row>
    </div>
</template>

<script>
    import Header from "@/components/Header.vue";
    export default {
        name: "Notification",
        components: {
            Header,
        },
        data() {
            return {
                NotificationList: [
                    {
                        title: "new message",
                        content:
                            "You have a new message.Please check the notification box.You have a new message.You have a new message.Please check the notification box.You have a new message.You have a new message.Please check the notification box.You have a new message.Please check the notification box.You have a new message.Please check the notification box",
                        read: false,
                    },
                    {
                        title: "new message",
                        content:
                            "You have a new message.Please check the notification box.You have a new message.You have a new message.Please check the notification box.You have a new message.You have a new message.Please check the notification box.You have a new message.Please check the notification box.You have a new message.Please check the notification box",
                        read: false,
                    },
                    {
                        title: "new message",
                        content:
                            "You have a new message.Please check the notification box.You have a new message.You have a new message.Please check the notification box.You have a new message.You have a new message.Please check the notification box.You have a new message.Please check the notification box.You have a new message.Please check the notification box",
                        read: true,
                    },
                ],
            };
        },
        methods: {
            read(item) {
                item.read = true;
            },
            back() {
                this.$router.go(-1);
            },
        },
    };
</script>

<style scoped lang="scss">

    .card {
        margin: 20px;
        &:hover {
            cursor: pointer;
            transform: scale(1.02);
        }
        h3,
        p {
            line-height: 2.5;
        }
    }
    .unread {
        background-color: #ccc;
    }
</style>
