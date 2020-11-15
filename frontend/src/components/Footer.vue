<template>
    <div class="footer" id="footer">
    <el-row type="flex" justify="center" style="align-items: center;">
      <div class="footer-wrap">
        <p>
          wwww.AlphagoAuction.com
        </p>
        <p>
          Copyright &copy; 2020. All rights reserved.
        </p>
      </div>
    </el-row>
  </div>
</template>

<script>
export default {
  data() {
    return {
    };
  },
};
</script>

<style scoped lang="scss">
.footer {
    width: 100%;
    /*margin-top: 30px;*/
    // padding: 15px;
    padding: 0px;
    background-color: rgba(18, 22, 43, 0.95);
    color: #fff;
    // position: fixed;
    /*position: absolute;*/
    /*bottom: 0;*/
    /*height: 20%;*/

    // font-size: 15px;
    font-size:10px;
    .footer-wrap {
        text-align: center;
        p {
            margin: 5px 0;
            // margin:0px;
        }
    }
}

</style>
