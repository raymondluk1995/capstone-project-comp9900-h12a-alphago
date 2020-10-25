<template>
    <div class="header">
        <el-row type="flex" justify="space-around" style="align-items: center;">
            <el-col :span="2">
<!--                <el-avatar :size="50" :src="logo"></el-avatar>-->
                <img @click="goto('home')" src="@/assets/Alogo.png"
                             alt="render failure" style="width: 100px; height:90px; cursor: pointer">
            </el-col>
            <el-col :span="8">
                <el-row type="flex" justify="center">
<!--                    <h1 style="font-family:giveny;color: #222222;margin-left: 20px">ALPHAGO AUCTION</h1>-->
<!--                    <h3 style="font-family:Georgia;">-->
<!--                        AlphaGo Auction-->
<!--                    </h3>-->
                </el-row>
            </el-col>
            <el-col :span="2">
            <el-row type="flex" justify="center" style="align-items: center;">
                    <slot></slot>
            </el-row>
            </el-col>
        </el-row>
    </div>
</template>

<script>
export default {
  data() {
    return {
    };
  },
  methods: {
    goto(name) {
      // console.log(name);
      this.$router.push({ name: name });
    },
  },
};
</script>

<style scoped lang="scss">
.header {
  padding: 15px;
  border-bottom: 2px solid #f2f2f2;
    box-shadow: 0 2px 10px 4px #d5dbea;
    /*height:120px;*/
}


</style>
