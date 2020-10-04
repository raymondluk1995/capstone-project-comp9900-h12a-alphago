<template>
<!--  <div class="header">-->
<!--    <el-row type="flex" justify="space-around" style="align-items: center"">-->
<!--    <el-col :span="8">-->
<!--        <img @click="goto('home')" src="@/assets/Alogo.png"-->
<!--             alt="render failure" style="width: 100px; height:90px; cursor: pointer">-->
<!--      </el-col>-->
<!--      <el-col :span="8"  >-->
<!--        <div class="slogan" >-->
<!--          <h4 style="font-family:Georgia;color: #222222">AlphaGo Auction</h4>-->
<!--        </div>-->
<!--      </el-col>-->
<!--        <el-col :span="4"  >-->
<!--          <slot></slot>-->
<!--        </el-col>-->
<!--    </el-row>-->
<!--  </div>-->
    <div class="header">
        <el-row type="flex" justify="space-around" style="align-items: center;">
            <el-col :span="6">
<!--                <el-avatar :size="50" :src="logo"></el-avatar>-->
                <img @click="goto('home')" src="@/assets/Alogo.png"
                             alt="render failure" style="width: 100px; height:90px; cursor: pointer">
            </el-col>
            <el-col :span="6">
                <el-row type="flex" justify="center">
                    <h3>
                        AlphaGo Auction
                    </h3>
                </el-row>
            </el-col>
            <el-col :span="6">
            <el-row type="flex" justify="center" style="align-items: center;">
<!--                <el-col>-->
                    <slot></slot>
<!--                </el-col>-->
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
  /*margin-bottom: 30px;*/
  margin-left: 30px;
  margin-right: 50px;
  padding: 15px;
  border-bottom: 2px solid #f2f2f2;
}
</style>
