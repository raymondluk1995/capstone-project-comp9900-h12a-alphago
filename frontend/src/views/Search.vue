<template>
  <div id="search-page">
    <Header>
      <template v-if="this.hasLogin">
        <el-dropdown
          trigger="hover"
          @command="handleCommand"
          style="align-items: center"
          placement="bottom"
        >
          <div class="user">
            <el-badge
              v-if="parseInt(this.unread) !== 0"
              :value="this.unread"
              :max="99"
              class="item"
            >
              <el-avatar :size="70" :src="avatar"></el-avatar>
            </el-badge>
            <el-avatar v-else :size="70" :src="avatar"></el-avatar>
          </div>
          <el-dropdown-menu slot="dropdown">
            <el-dropdown-item command="profile" icon="el-icon-user-solid">
              My Profile</el-dropdown-item
            >
            <el-dropdown-item command="property" icon="el-icon-house">
              My Properties</el-dropdown-item
            >
            <el-dropdown-item command="auction" icon="el-icon-s-home">
              My Auctions</el-dropdown-item
            >

            <el-dropdown-item command="notification" icon="el-icon-bell">
              Notifications
              <el-badge
                v-show="parseInt(this.unread) !== 0"
                class="mark"
                :value="this.unread"
                style="padding: 0; background-color: white"
              />
            </el-dropdown-item>

            <el-dropdown-item command="logout" icon="el-icon-turn-off">
              Log out</el-dropdown-item
            >
          </el-dropdown-menu>
        </el-dropdown>
      </template>
      <template v-else>
        <div class="back-btn">
          <span
            id="back-btn"
            style="padding: 2px 5px; font-size: 20px"
            @click="goto('login')"
            >Sign In <i class="el-icon-check"></i
          ></span>
          <div class="bottom-line"></div>
        </div>
        <div class="back-btn">
          <span
            id="back-btn2"
            style="padding: 2px 5px; font-size: 20px"
            @click="goto('register')"
            >Sign Up <i class="el-icon-user"></i
          ></span>
          <div class="bottom-line"></div>
        </div>
      </template>
    </Header>

    <div id="main" justify="center">
      <template>
        <!-- Search Bar Part  -->
        <el-row
          type="flex"
          justify="center"
          class="search"
          style="margin-top: 10px"
        >
          <el-col :span="20" justify="center" type="flex">
            <div>
              <el-col :span="20">
                <vue-google-autocomplete
                  ref="address"
                  id="address"
                  classname="form-control"
                  placeholder="Please search suburb name or postcode here"
                  v-on:placechanged="getAddressData"
                  @keyup.enter="toSearch"
                  country="au"
                  types="(cities)"
                >
                </vue-google-autocomplete>
              </el-col>

              <el-col style="margin-left: 2px" :span="2" id="search-btn">
                <el-button
                  style="
                    height: 38px;
                    color: white;
                    background-color: rgb(20, 60, 127);
                  "
                  icon="el-icon-search"
                  @click="toSearch"
                >
                  Search
                </el-button>
              </el-col>

              <el-col style="margin-left: 10px" :span="1" id="search-btn">
                <el-button
                  style="
                    height: 38px;
                    color: white;
                    background-color: rgba(16, 117, 244, 0.7);
                    margin: 0;
                  "
                  @click="showFilter"
                >
                  <i class="fas fa-filter"></i> Filters
                </el-button>
              </el-col>
            </div>
          </el-col>
        </el-row>

        <!-- Filters Part  -->

        <el-row type="flex" justify="center" style="margin-top: 10px">
          <div id="filters" v-show="showFilterFlag">
            <el-row :gutter="3" type="flex" justify="center">
              <el-col :span="5">
                <el-popover placement="bottom">
                  <el-date-picker
                    v-model="dateFrom"
                    type="date"
                    placeholder="Choose the Start Date"
                  >
                  </el-date-picker>
                  <el-button
                    style="
                      color: white;
                      background-color: rgba(20, 60, 127, 0.9);
                      margin: 15px;
                      padding: 5px;
                      border-radius: 10px;
                    "
                    slot="reference"
                  >
                    <i
                      class="fas fa-calendar-alt"
                      style="margin-right: 10px"
                    ></i
                    >Start Date<i class="el-icon-arrow-down el-icon--right"></i>
                  </el-button>
                </el-popover>
              </el-col>

              <el-col :span="5">
                <el-popover placement="bottom">
                  <el-date-picker
                    v-model="dateTo"
                    type="date"
                    placeholder="Choose the End Date"
                    :picker-options="pickerOptions"
                  >
                  </el-date-picker>
                  <el-button
                    style="
                      color: white;
                      background-color: rgba(20, 60, 127, 0.9);
                      margin: 15px;
                      padding: 5px;
                      border-radius: 10px;
                    "
                    slot="reference"
                  >
                    <i
                      class="fas fa-calendar-alt"
                      style="margin-right: 10px"
                    ></i
                    >End Date<i class="el-icon-arrow-down el-icon--right"></i>
                  </el-button>
                </el-popover>
              </el-col>

              <el-col :span="5">
                <el-popover placement="bottom">
                  <el-select
                    v-model="bedrooms"
                    placeholder="Select"
                    class="selects"
                  >
                    <el-option
                      v-for="item in options"
                      :key="item.value"
                      :label="item.label"
                      :value="item.value"
                      justify="center"
                    >
                    </el-option>
                  </el-select>

                  <el-button
                    style="
                      color: white;
                      background-color: rgba(20, 60, 127, 0.9);
                      margin: 15px;
                      padding: 5px;
                      border-radius: 10px;
                    "
                    slot="reference"
                  >
                    <i class="fas fa-bed" style="margin-right: 10px"></i
                    >{{ bedrooms }} Bedrooms<i
                      class="el-icon-arrow-down el-icon--right"
                    ></i>
                  </el-button>
                </el-popover>
              </el-col>

              <el-col :span="5">
                <el-popover placement="bottom">
                  <el-select
                    v-model="bathrooms"
                    placeholder="Select"
                    class="selects"
                  >
                    <el-option
                      v-for="item in options"
                      :key="item.value"
                      :label="item.label"
                      :value="item.value"
                      justify="center"
                    >
                    </el-option>
                  </el-select>

                  <el-button
                    style="
                      color: white;
                      background-color: rgba(20, 60, 127, 0.9);
                      margin: 15px;
                      padding: 5px;
                      border-radius: 10px;
                    "
                    slot="reference"
                  >
                    <i class="fas fa-bath" style="margin-right: 10px"></i
                    >{{ bathrooms }} Bathrooms<i
                      class="el-icon-arrow-down el-icon--right"
                    ></i>
                  </el-button>
                </el-popover>
              </el-col>

              <el-col :span="5">
                <el-popover placement="bottom">
                  <el-select
                    v-model="garages"
                    placeholder="Select"
                    class="selects"
                  >
                    <el-option
                      v-for="item in options"
                      :key="item.value"
                      :label="item.label"
                      :value="item.value"
                      justify="center"
                    >
                    </el-option>
                  </el-select>

                  <el-button
                    style="
                      color: white;
                      background-color: rgba(20, 60, 127, 0.9);
                      margin: 15px;
                      padding: 5px;
                      border-radius: 10px;
                    "
                    slot="reference"
                  >
                    <i class="fas fa-car" style="margin-right: 10px"></i
                    >{{ garages }} Garages<i
                      class="el-icon-arrow-down el-icon--right"
                    ></i>
                  </el-button>
                </el-popover>
              </el-col>

              <el-col :span="5">
                <el-popover placement="bottom">
                  <el-select
                    v-model="type"
                    placeholder="Select"
                    class="selects"
                  >
                    <el-option
                      v-for="item in propTypes"
                      :key="item.value"
                      :label="item.label"
                      :value="item.value"
                      justify="center"
                    >
                    </el-option>
                  </el-select>

                  <el-button
                    style="
                      color: white;
                      background-color: rgba(20, 60, 127, 0.9);
                      margin: 15px;
                      padding: 5px;
                      border-radius: 10px;
                    "
                    slot="reference"
                  >
                    <i class="fas fa-info-circle" style="margin-right: 10px"></i
                    >Type: {{ type
                    }}<i class="el-icon-arrow-down el-icon--right"></i>
                  </el-button>
                </el-popover>
              </el-col>

              <el-col :span="5">
                <el-popover placement="bottom">
                  <el-row>
                    <div>min ($k)</div>
                    <el-input-number
                      v-model="minPrice"
                      :min="0"
                      :max="maxPrice"
                      size="small"
                    ></el-input-number>
                  </el-row>

                  <el-row>
                    <div>max ($k)</div>
                    <el-input-number
                      v-model="maxPrice"
                      :min="minPrice"
                      :max="100000"
                      size="small"
                    ></el-input-number>
                  </el-row>

                  <el-button
                    style="
                      color: white;
                      background-color: rgba(20, 60, 127, 0.9);
                      margin: 15px;
                      padding: 5px;
                      border-radius: 10px;
                    "
                    slot="reference"
                  >
                    <i class="fas fa-dollar-sign" style="margin-right: 10px"></i
                    >Price<i class="el-icon-arrow-down el-icon--right"></i>
                  </el-button>
                </el-popover>
              </el-col>

              <el-col :span="5">
                <el-popover placement="bottom">
                  <el-row>
                    <div>min (m<sup>2</sup>)</div>
                    <el-input-number
                      v-model="minArea"
                      :min="0"
                      :max="maxArea"
                      size="small"
                    ></el-input-number>
                  </el-row>

                  <el-row>
                    <div>max (m<sup>2</sup>)</div>
                    <el-input-number
                      v-model="maxArea"
                      :min="minArea"
                      :max="100000"
                      size="small"
                    ></el-input-number>
                  </el-row>

                  <el-button
                    style="
                      color: white;
                      background-color: rgba(20, 60, 127, 0.9);
                      margin: 15px;
                      padding: 5px;
                      border-radius: 10px;
                    "
                    slot="reference"
                  >
                    <i class="fas fa-home" style="margin-right: 10px"></i>Area<i
                      class="el-icon-arrow-down el-icon--right"
                    ></i>
                  </el-button>
                </el-popover>
              </el-col>

              <el-col :span="5">
                <el-popover placement="bottom">
                  <el-select
                    v-model="order"
                    placeholder="Select"
                    class="selects"
                  >
                    <el-option
                      v-for="item in sortTypes"
                      :key="item.value"
                      :label="item.label"
                      :value="item.value"
                      justify="center"
                    >
                    </el-option>
                  </el-select>

                  <el-button
                    style="
                      color: white;
                      background-color: rgba(20, 60, 127, 0.9);
                      margin: 15px;
                      padding: 5px;
                      border-radius: 10px;
                    "
                    slot="reference"
                  >
                    <i class="el-icon-sort" style="margin-right: 10px"></i
                    >Sort<i class="el-icon-arrow-down el-icon--right"></i>
                  </el-button>
                </el-popover>
              </el-col>

              <el-col :span="5">
                <el-button
                  style="
                    color: white;
                    background-color: rgba(127, 127, 127, 0.9);
                    margin: 15px;
                    padding: 5px 10px;
                    border-radius: 10px;
                  "
                  @click="handleClear"
                  ><i class="el-icon-error" style="margin-right: 10px"></i>Clear
                  Filter
                </el-button>
              </el-col>
            </el-row>
          </div>
        </el-row>
      </template>

      <!-- SEARCH RESULTS -->
      <template v-if="!isEmpty">
        <el-row type="flex" justify="center" class="result-rows">
          <div class="items">
            <ul :class="colNumObject">
              <li v-for="item in propList" :key="item.aid">
                <v-card
                  :class="vcardObject"
                  max-width="2000"
                  @click.native="goDetails(item)"
                >
                  <div
                    style="
                      background-color: rgba(128, 128, 128, 0.3);
                      padding: 1px;
                    "
                  ></div>
                  <el-carousel
                    :interval="5000"
                    arrow="always"
                    class="carousel"
                    style="margin: 2%"
                  >
                    <el-carousel-item v-for="pic in item.photos" :key="pic.id">
                      <img :src="pic" width="100%" height="100%" alt="" />
                    </el-carousel-item>
                  </el-carousel>

                  <el-row justify="center" type="flex">
                    <v-card-title>{{
                      decapitateAddress(item.address)
                    }}</v-card-title>
                  </el-row>

                  <el-row type="flex" justify="center" class="width:100%">
                    <el-col :span="3" style="margin-right: 10px">
                      <i class="fas fa-bed" style="margin-right: 5px"></i>
                      {{ item.bedroomNum }}
                    </el-col>

                    <el-col :span="3" style="margin-right: 10px">
                      <i class="fas fa-bath" style="margin-right: 5px"></i>
                      {{ item.bathroomNum }}
                    </el-col>

                    <el-col :span="3" style="margin-right: 6px">
                      <i class="fas fa-car" style="margin-right: 5px"></i>
                      {{ item.garageNum }}
                    </el-col>

                    <el-col :span="5" style="margin-right: 10px">
                      <i class="fas fa-home" style="margin-right: 5px"></i>
                      {{ item.area }} m<sup>2</sup>
                    </el-col>
                  </el-row>

                  <div v-if="item.status === 'A'">
                    <v-card-text>
                      <div class="my-4 subtitle-1 result-type">
                        Type: {{ item.type }}
                      </div>
                      <div class="my-4 subtitle-1 result-type">
                        Auction Ends At: {{ showTime(item.endDate) }}
                      </div>
                      <div class="my-4 subtitle-1 result-type">
                        Attended Bidders: <b>{{ item.bidderNum }}</b>
                      </div>
                    </v-card-text>

                    <v-divider
                      class="mx-4"
                      style="margin: 0; padding: 0"
                    ></v-divider>
                    <v-card-text style="padding: 5px; margin: 0">
                      <el-row
                        type="flex"
                        justify="space-around"
                        class="current-price"
                      >
                        <div class="bid">
                          Current Highest Bid: <br />
                          ${{ getBidStatus(item) | numFormat }}
                        </div>
                      </el-row>
                    </v-card-text>
                  </div>

                  <div v-else>
                    <v-card-text>
                      <div class="my-4 subtitle-1 result-type">
                        Type: {{ item.type }}
                      </div>
                      <div class="my-4 subtitle-1 result-type">
                        Auction Starts At: {{ showTime(item.startDate) }}
                      </div>
                      <div class="my-4 subtitle-1 result-type">
                        Try To Be The First Bidder!
                      </div>
                    </v-card-text>

                    <v-divider
                      class="mx-4"
                      style="margin: 0; padding: 0"
                    ></v-divider>
                    <v-card-text style="padding: 5px; margin: 0">
                      <el-row
                        type="flex"
                        justify="space-around"
                        class="current-price"
                      >
                        <div class="comingSoon">
                          COMING SOON<br />
                          PLEASE WAIT
                        </div>
                      </el-row>
                    </v-card-text>
                  </div>
                </v-card>
              </li>
            </ul>
          </div>
        </el-row>
      </template>

      <template v-else>
        <div style="margin-left: 10%">
          <h3 style="color: rgb(102, 102, 102)">No Results Found</h3>
        </div>
      </template>

      <el-row justify="center" type="flex">
        <div class="pagination">
          <el-pagination
            background
            layout="prev, pager, next"
            :page-size="pageSize"
            :total="total"
            @current-change="handleCurrentChange"
            :current-page="currentPage"
          ></el-pagination>
        </div>
      </el-row>
    </div>
  </div>
</template>



<script>
import Header from "@/components/Header.vue";
import { mapActions } from "vuex";
import $ from "jquery";
import VueGoogleAutocomplete from "vue-google-autocomplete";
import numFormat from "../utils/numFormat";
import dayjs from "dayjs";

export default {
  name: "Search",
  title: "Find A Property",
  components: {
    Header,
    VueGoogleAutocomplete,
  },

  data() {
    return {
      unread: "",
      isEmpty: false,
      hasLogin: false,
      searchPropertyList: [],
      filterPropertyList: [],

      //pagination starts
      total: 0,
      pageSize: 6,
      currentPage: 1,
      showPropList: [],
      // pagination ends
      search: "", // the search condition for whole page
      type: "Any",

      // variables for filter in Search
      dateFrom: "",
      dateTo: "",
      pickerOptions: "",

      options: [
        {
          value: "",
          label: "Any",
        },

        {
          value: "1",
          label: "1",
        },
        {
          value: "2",
          label: "2",
        },
        {
          value: "3",
          label: "3",
        },
        {
          value: "4",
          label: "4",
        },
        {
          value: "5+",
          label: "5+",
        },
      ],

      propTypes: [
        {
          value: "",
          label: "Any",
        },

        {
          value: "Apartment",
          label: "Apartment",
        },
        {
          value: "Commercial",
          label: "Commercial",
        },
        {
          value: "Unit",
          label: "Unit",
        },
        {
          value: "House",
          label: "House",
        },
        {
          value: "Studio",
          label: "Studio",
        },
      ],

      sortTypes: [
        {
          value: "",
          label: "Default",
        },

        {
          value: "+price",
          label: "Price: Low To High",
        },
        {
          value: "-price",
          label: "Price: High To Low",
        },
      ],

      bedrooms: "",
      bathrooms: "",
      garages: "",
      order: "",
      suburb: "",
      postcode: "",
      minPrice: 0,
      maxPrice: 999999999,
      minArea: 0,
      maxArea: 999999999,

      // variables for filter Search ends
      filterFlag: false,
      address: "",
      state: "",
      colNumObject: {
        // twoColUl: true,
        twoColUl: false,
        // oneColUl: false,
        oneColUl: true,
      },
      showFilterFlag: false,

      vcardObject: {
        "mx-auto": true,
        "my-12": true,
        cardWidth40: false,
        cardWidth: true,
        vcard: true,
      },

      propList: [],

      pickerOptions: "",

      searchBase: "",

      originAddress: "",

      suburbOrPostcode: "",

      tempPostcode: "",
      tempSuburb: "",
      tempState: "",

      // propList: [
      //   {
      //     aid: 1,
      //     status: "A",
      //     bathroomNum: 2,
      //     bedroomNum: 1,
      //     garageNum: 2,
      //     type: "Apartment",
      //     area: 123,
      //     address: "2 Gearin Alley, Mascot, NSW",
      //     photos: [
      //       "data:image/jpeg;base64,/9j/4AAQSkZJRgABAQAAAQABAAD/2wCEAAkGBxISEhUTEhMVFhUVFxUVFRcYFxYVFxcWFxcWFxUXFhgYHSggGRolGxUVITEiJSkrLi4uFyAzODMsNygtLisBCgoKDg0OGxAQGy0lHyUtLy0tLS0rLS0tLS0tLS0tLS0tLS0tLS0tLS0tLS0tLS0tLS0tLS0tLS0tLS0tLS0tLf/AABEIAKgBLAMBIgACEQEDEQH/xAAbAAABBQEBAAAAAAAAAAAAAAAEAAECAwUGB//EAEQQAAEDAgQDBQYFAQUGBwEAAAEAAhEDIQQSMUEFE1EiYXGBkQYyobHR8BRCUsHhgiNDYpLxBxUzU3KyJGNzoqPS4hb/xAAZAQEAAwEBAAAAAAAAAAAAAAAAAQIDBAX/xAAoEQACAgICAgEDBAMAAAAAAAAAAQIRAyESMQRBURMyYRQiQuEzgbH/2gAMAwEAAhEDEQA/AOMDVINVoYphikFTWq1rVIMVjWICGRSDFblUwxAUhqkGq4MThiAoyKQarsim2mgK2U5V3KU6TESymgBhSUxSRjaaly0AGKajy0dy03KQARppuWjjSUeUgBOWly0Zy03LQAnLTimiuWnFNACimpCmiuWpCkgBRSUuUihTT8tACiknFNFCmn5aAFFNPy0UKakKaAE5Sly0Xy04poAUU1IUkSKalkQAopJ8iJyJZEAPkSyIjIlkQHG8lOKS0hh0/wCFUgz201MU0WcOkKSAoFNTbTVwpqxrFAKBSUxSRTWKwU0AEaSk1iMNJIUkAOKavY1WimphiArDVaGqYYrGsQFORSbTVwYptagB+Sm5SC4j7QUqfZZ/aP6D3Z73fSUTwviDaoANn9Nj1ifkgLOUm5SNyJuWgAuUnFJGctPykAGKSlykXy0+RACCmpctE5FLloAUU04pooUk4pIAUUk4posU1IU0AIKSflowUk/KQAfLT8tGCkn5SAC5abIjTSS5KAD5afko8UU/KQHNCinFFaAoKQooDN5CRwoWnyE/4dAZJwqj+HK2hh0/4ZAYzaasaxapwgUThEBnctOKaOOHUTRQAoYphiv5SkGJQKmsVjWLOx/G6VOw7bujdAe8/SVz+Ox9at77srf0tsPPr5oRZt8R49Sp2b/aO6N0nvd9JXP43GVq3/Edlb+lth59fNDB7RZgk/e6mzBvf72nQJfwBYcsByt13OvqVpU6W4N/v7lW8N4YARbr8kS/BOaTGnRSA/h3EZ7NSx2PXx+q1uWucY0O8R6haWAxhZDX3bsdSP47vRKFmjy0uWimtBEi4UuUoJBOWn5aKFNOKSAGFNSFJFCkpCkgBBSUhTRYpJxSQAopJxSRYpKXLQAopJxSRXLUhSQAnLT8pFikpctABcpSFJF8pOKaAEFJS5SK5afloDC5KcUkbykuUgBBTTimi+Wn5aAGFJOKaJFJOKaAG5aY00Xy0jSQAJYhcRi6THBrnAE3/wBei0sS0taS1uYgSGixPqvLfaPijqlUmADFwHG2pOu6yyZONUVlKjWxHtFWZVLHMadB2YcJ8Qe8dUNxDiNWrIecjf0tt69fNZmExxc4EC0EiLAQR9Qi8E5tZzmEaXPS0R3RF/NZQytOikZX2DB40YJ7/wCURR4c9/vHyGi2KWHpN3Hlc+gRIrAaMd5gN+Zldaj8lrAcNwwDZHtwulh0sIU2PqHRrQP830TBjyLvPlDR8AfmpHQXhcPDh97Ix2GmfJZuFwjc0m+t5JPrmt8FptquZr2m7zAcOlzY+ceaUSmA1uHXJFjP7BUNaRZ419Cugoua+Y8xoRYag3QPFmEUnFgBcGy2dJGkwgZHBOdT0u3cbjvH39VtUYcJaZC4DC8ZxQ3w3q/6rRwvGMUHSPw/eJdB8iUohM7IUlIUlz1LjeKP5cP/AJnf/ZF0sfjHCRToHwc76qOLJtGyKScUkFwDi4rhzXtyVWGHs/cTqFsBigkHFJLlIoMUgxCQQU0/LRYppZEAMKafIiAxPkQFAYlkRGRPkQA+RLlokMTimgBhTUuWieWlkQGRy0zmQCel0Xy05pSI62UA4D2g9qaZAbQcZbLibt7mi3n8F0nBMe2tSY6RnLQXDf8A10JXlvEsOGVXUrGH1BmmXdlxECdojzW7wL2hfh2tpQ0ZyAXusR3xoIne65o5GpNsyU97PRalIwcsAwYJ0mLH1WRwN1Xk1OZctzQ6xM3mR1B9VvBhZTlxzENJLgBJ7wAuQ4HUcznU3AtLyO04BoaRrJk7HTfzJW0mXbD/AGdxr3nIWuIiczrehiCZtboVvcpctwPH1KDKjXNOUZgHubBNWCWg9ZMD0Vz/AGixNJk1MOXAZe1oJM9kESDcfFUjkSVMhSSNsuAfkd+b3ehs4kegPx6LybjhazE1czQf7Qg7aPF4jumy67jxrYhor0XFzeycrQSWASHHL1Fv80TMriMW5jz+YF1x0MRM92m+6xy5LktGc5FFNg95o7OkgHe5m8jTraFrcBpMAc97mwMovabkzfXuQVKi5rRncBrLIght79Ce7uWpgKbHhmHybyTmAOhBN+6FTHK5WUT2blKiIBGhHgPoreTb7/ayKo4YMAaBAFhr+ysNKenw/hemrrZoVUaPZ8nfuqxT0n9v3j5ok0HaOe8jxyj4NU2s6fD+D+ykmyrDU+0PPr87/NS4hRzU3NkDMC2SYAzAiSel9RPiiKNPtC3w/wDzKuc3+f5/k+SEmHwnAmlRYx0BzBEtMx0yuG0QpYviYbFOoZLgcsCHECCbRB8oPdZaJpgaCJv/ADH76LH4rgg57ahmWh0REdoQZ36XQWYjKDASZ38Ve11MXv6fVMzDnYNN7h2bS+haDedjZWNoHekB5uBUPQQTg6gd7rbSASbekhbeHrcs5QROsTqCfguc/Dj/AJfo7+EhRb+h48CPootEm7jXCqRWoZhXZYOgwRu1+0Lp+BcUZiWmOzUYctRhsWnw6HVefNpM/wDMHp9VOmQ0y2pUaTaYdPqCjcWFaPU8ipqU327QFwCAJNzGpP7LztuNqDTFVB4morP954jbFnzd9Qo18k3+D0gMSyLztvGcZtimnzpn5hWt47jx/fMP9NIpS+ST0DInyLgm+0nEB/yz/Q39irG+1uNGtJh/od+xShZ3WRPkXDj22xI1w7fSoE49vqo97DN/zPHzalC0dwGJ8i4pv+0WNcN/8n1armf7RqW+Hf5Oaf2SmLR2Ipp+Wsj2e9pW4txbTo1WhvvOcBlHQTue4f67xIGpAVWScdifaBlPFtwzmwCB2zu4xAHdeFugDqF57xP2koVw38RTqMqU5yFsGXWIF4IG8jqgH8aLgQyWdntwcxJIEgyMpE3jxuYWDzKKbM+Zme1GCDMXULhmGZ1RhEdsOAMucD1+ZCbhuIz1aYd2xBIZmJJJMkFxvsXSsvjOIBc12d0RAEeFgZtuVPh2JYYLqYkbyYNobYeGt1yvJ/JGLluz0dnGKVF7m0quZhbHaLg4OJdBaSNL6gd64/FV3se5tQjZwDXdm7SQ5u2kfFC4vEibhpJ7UxfzO6pxeEJh7SC24tYmYHu7bdVm83LvRPKzcweNphhmsdRs/MZAzAtJLYBzGQZJ6LoqWOwrcPkio5jS5wLjkEkEXE3jNoVxNGiGtYXVCHjtFpB2HURufglV4hVDS0HMTdrgJIj3RHed1ZeQ0/2qxyoO/wB8Mw9Osym9wfUsxrA2AHOklxkmYMQP0g22559EkAvfo3K0CDlDdBPXUW6+hOIzZc2SHC413ifG9kJXoPphuewvqWnQz7s21KSyuVbKttjASfek6WOuxjrstbCVTSbLSAfeLe1mkGCfCCTbvWNhy4k5GudGzRMCbCQZ1U8JQLiS7MD+YEdxse6w9VV60mRWzrsLiauJLB2YmXZSYixuR42W/WxLG2LhPSfqFx/DqjGbCTF3Rl7tdt0fhKQe8yHFwOYdIJ0iB0PqtIeRKMaW2dMMfKqe/wDh0LMXTOjh5Xjxymyg7Hs/xH+l1/8AMFRnMEiWsMZmgl0i3UgEeJCjWze6C7KCYFxI0zQF1LyJJHdHwo3thDOICYDHeYaOnf37hWu4gzeW/wDUC2POI9AgT2WZuy6BnJaSXCPyu2CDf7V4ftZuccwGbMALC85jv3zv3rTHlnL4KZsGOC1ZtPd9/UfufRB4qpb7+ysmr7YYcf3TzH+IR8JA8lXhfa2jUqMp/h7ve1klxPvEC/Zvquo4TWwJAaPP5rQpYYOGYXm8rgfajiJoV3NLoaT2WhoblEAEhwFwZNtbTKK9n+N1pDaRaAbuBLAGjoC9wB8uvcuRZ5KXGa0IW5UdscAOir/3eL2Qj+KOzCH1MhDZkUmXMzDspGysx2INJ45jnhjvdBqNaTbXM1o6jZXeeCOpeLk/BceHhVP4eLW+4KysTxNmvPEHRvOdLbeIBQr+J0D71Znm8uj46Kj8iPpM1/RT9yRtuwIHRD1aFMauaPEgLLw3EcKXQ+tSaBEk9o9DlDWnNppbxTYzjWCDpY9zwOrC2/UAAzYb9VP1bV8SP0yTpzRoPoUwJJEddr6XQtR1Eb+gJ+QTv9pMABDXYpwgWgjYDemYhZT+PUZECoWzeWSRc2vE2+ah5H6iI4Ye5Bz6lMaB3kx30QzsUBoKnkf5VVbjtEEZKdV/6gWhgIGmj5/0Sqe0rDOXBx4uJ9ZcdkufwOGJey52IqjaoPF421371WcdWDgC4tBOpdmjXaO5Z1XjJOmGG+4t4WsnoY7m1AzkBsyZNQnQT07lD51dFoxxN0n2GYvGV23bXY4RpEGb2gi+yM4IyrVrU21qjWsLmh3YbmynWDeFKtgmkSDftTeNACbHQa6Jjgg1jyHtADXEmbWbIkgTqs/qM2l48VFtHqVXjOEwlHJSLSGA5WMl0nxGpJ8yvJOL+1OKr1XVBULBoGiLAeRQmLxNQHKHt6EZoPn97lBDC1jfLrNw5omDG5G4W0JZP5KjgSXsufjOZ2pBIvfaNoOuihVxUtH5ZBMf+2T3mDp1VlcvI1BGwn3trjSUAKQLu1OXS3Xv6BeY5dr+zjsvwlWm6WvAds3MDE90GxVjsMQczS0NFgD8W+AusupQIcDSu0XvqND2vgtKjFVrnVAWmTEQP8p3E5vh1SSraegFvql12sgxBs0x84QvMdm2Ea+XSNFCTpIA2gxMKAmCDv02A/mFWMbAaRVcS7PcdRsesa6lTD3sGabuFh+mBtqBYi/f1QdPExI1nfumY71Gs8kjs3yidydLx5qHF3REkRx/EHSGyYAv0nczuo1ceHtdnESRtEQBEd9hfxVD2h7mtJIJdffvVuJwbQ1zWum1wdhJ1GxmdeiulGNaI0izBY7lZiwlpdEnQQ24AgWO6KGOMgubJF/hMH5fXfPLmZgLxpEDpIMnTYIlsF0Xn3QBuZtpqe/w0upcF2Suw4YluYB7TJ6H+oZo1tEhbVPE5g5rKfZDXdrMDALCRrPWxWZS4MXBrsjqRJBvJbF5dIHiPIrSwuGe0EOcD2XCwsTG0xGmkTdRGH7rR2ePjmpWk69gXslTc6rVNQVC2jVgP1DQLAElwzOJgR4LsamEuQWOzES2zW90BzoBnovMOD49w4iGZuyK9SGyQCTm20mwMnovRMTRdywcoDdM8ATeYLhcmV35WrOrxbadv2KmYY5skZhDgTAb4gQCfGVyXHMLFXLlvqCAACDcOaNgRfvXVPq8wwXB0gNzG8d5nbXW2q53jjmCG9jPcyINMDKMvuSS6Sb/AAMysod6OufR57Qxxc8NLYJ307+gPxR/Darhi8MA4wa1OQTP5wp4mf1fBqp4Wf8AxeG/9al/3hd55DOw/wBoFFpexx17UToYcLdZuTbogvZfECm900mmQCJMxF7RaCCr/bp3MrNp9khsmDqCZMwCLQO5BezNEh7iHNAykOZoXR0BE9FyZZfvdEeP/lRr+0jBWfTLwywfEWgdmGwdgZjxKz6fCaX6G+n8LQ4i69PWweP+3ooMP3ddGDcEbeUqytFLOGU/0tVgwDOgRA+9U4H3db0jnKBgm93ol+Db0Hor8v3dLL93QFH4UfYSOHH2AriO/wCajl+4QFJoD7hRNIfcK0t+4USPuFAKXUvFT4W0c9lv1b/4T0TPH3CfhbAcTSBAdJd2TIB7DuhVMn2s1w/ejr8FUrDOKLXDMTeWuvtGaZWViWOl9OpF+ycw7YkRbLZbPEeIsfTyNNRsNLS0ZMpiJzTJgdywwWwbui0Rlyzl3m/VefyVpHrRg+LbVDYnB0TmJpUpLaZktaSLulxtvp5Imph6JP8AwqYAkAZG2EkwLd5PmgqryWuJ15NH/uqK6s4Zj97BdsWzyZI57EYKl+VzmnWZBF9bCb+KZnC2O96qZBsbekddEC7F5DdzDYdkT4Ha6HdxuxDc4O0CR0ES79l5PCb6OLbNOrwSl2stV03AEDXW4F/K26qp4AH3a8wSSYls2sSD8Vj4epUfmEtbMZiQGm21hvfdatKrW/4buydQCHbREdy04SWnIl2i/EYKGyKsiJINtCZifArMp4h1w2TqDGp6z3K7EcOaQQ2q4PBkTlykXEehmSd1k4XnU+y5rmTa7bg6AmVfHC1pl4o1KJJdJME2H+HvPoi21G7mdSTpIGuui5utWex0WEzE7kWm/gUGa9zDjmOwAIHmrrA3K2WUXZs4rFhjpywNnAdkSDHa03urGGswl14gzI6wYIOp0uhuFcMq1g7I3TXQ/D1RVFr5axxmCZ02AbfvgC8bBWnjS6JcFYfg+Dio0Pa8h1yW5dYJHlq1aFWo+n2WUgHNl2aHPJgEiSb2zdArMN2KeXszJJIuYnwunaT1ve1tN9f3+Cw5tNr0IupWaWA4s5zWue4OfAEkgm2wkW7vmo4rHF+ZrSNDJtI8Z9PNZxrtDZaPCYn4KqjWcTmDARJk2Pa89NQqO27OjPnldJ6AcMDSr1TzGODnuJyg52um7b93z2XTcIxpJDu05zbmCYAG4Iv0usPGNaXBwF7u7PU62jW6t4TXIfbS52A8D5nRaS3swxyamm/k66rjSGOIDZzTJu8XmOp0CwuMVKZF7vcO04HPJF5B90lx6G1/PTNWRFQwz3oJyg7QHEW1WTxHHV3B0AHMA0uaQ6YF5c032la4las9bNOMTj8WfD4ITgz5xeH6c6mJ1E5mmLb3HqtbGUXCMrSbDNNusgToNL62Q3CsDUfiqBLA0NqMOYZobD80WsJJjquvmmeWzZ45Ua/F1M5ylr7AizmiRodTfotDgYZTL3QzNEBj2F2wmJ7I01QvtDRY/EOextRxDsrsrX6jpIgnZdDwn2axVRodyMhP5nOyvIi0sOmuvcuecGpN9mniJRm5SMzikf2fXtWnNbsx4b6KimujxXshi3ZYptGXNPaaJmI87FUf/wAnjB/dejmn910YNQSZbyXyyNoyWgqUFah9msYP7h3llPyKg7gOKH9xU8mk/JbWc5nwUoRbuGVxrRqj+h/0VL8JVGrHjxa4fslgoKYqw0nDY+h+ihdARIKrIVk/dlEn7slklLvu5T8MoOfiKbW6uLo6e643nwTvKbAPIrsjq4axMtKpk+1mmH71XydJi6LmmKjIJzGZaRpBIgz9hDO4bWNM1A05cubNa7Q3aNlW8donKSTPQbb7QlicSBSLTRpzB7fbzTlN7OiV5sXctntZOUYaB6lSA6WyOSyYJJPadaAdBcz3lLE4qHuGRzr6jwHes2ni3EGxINOmGx+oFxdB8wijUbmcS8XM+FgI17l2clHs8WTSOQa4QSSCde1O21/uyrp4Zrml1TMDtcQR3WVj8QS74E2t63VOLq9oGTY6WOvztsuJWctBeFphkEvbJ0kZiHeRjpdTqPEQXkkakeIAkFDNxEAuLbmwkAOA3gbKNKoxxloNx+Zoi22bf5+Kuot7YotoPJt2jOsWHSbahEUuHMBDs8mQRmc49/eFOjiSIaR2fQ7REHTuV+It+VgAECxn5qybujTH3QHWoNLjIBImCVOjlGlMeNu4JNaTYD72Wnw3BMcf7V1Qadmm0EkbyXPAGnetm6R2qLfQO0WLsgAAnbTwU8O9rhAjMbiL7IjF8ODLtLnNAuXNDTrAs17u5Qp4fJDmPEzBAt8xdZ22uysoSXZJ1YEwHWk2Ai42hWsaAy14FrkmY8LpsW4GwG/QGbXlQw9d2QZWjRp8JAOsd65u1f5ORvod+FcGkuEZu+5PglTjLABi86300VpBcLFoIsSe/wCappVDPvDWCQD522Utt9kSleynF4sN0piSLuMj5KulUv2ST1t+/RV8SLmuMvBEWF9+68qOHrtES6SfL4nUK6hSIUWbdFuYBxB03B8pUn1wNHabWCBdWDiAJ0Gmp9BddDwj2Pq1YL/7JvU++R4fl8T6LeK1R2xSUVZjfirwBJPjr01+q3uC+z9d8Oe/lt6fnI8Bp4m66/hfs1RoxkZJ3cbuPmt7DYIDYK3GyG16MXh/AKTLtDp/Ubnvjp5LWGBHU/FHhgGydzJ/hWUUigG3DdCfVTFMj8xRDiAh3VvuykFbnvHT1VX4irO0eIV+Ydyrc7ooZKIc+p4q+lWfuEmURvCiaROgHqiVBl2adgmNIHVjfQKVKgRqVdkViAJ+DpnWkw+LQqH8Mob0KR/pb9FpZT0+KRb3pbFGK/geFOuHp+g/ZBYj2cwUyaAGt2lw1t1hdDV8UM5s7/BLYWnaOfd7P4XbmN8CDt3hAYv2YoOBitUHi0O+i6mpS/6fQoWpR8FnxXwbfVl8nnvHOBU8NSdVbWzQGMDSzKTBMGS7vuuMr46DcEzeQBvK9R9quHmvS5bYBBDr6GAYb3TOq8rxNJ1J5Y8ODgbiP4Wc1bOXJF2DVMSwuJIAEyLb7jxVeHDZM6i4F7+fT6Ltsf7DVmmBTa9sXIc0z5GDCwsRwQts5hZA1IjoBEqOEfTLvB+TMpFoIGWZiQ5wIA7um/onrVMlR3ZA5gtkcIj7k6jVWu4U4mMw7oHyICTPZ989rMNRe0W71KgU/TyK8FDmk3zCxl0nfc+KmyoSRLY9dfNWYfhtNmkuPTUI2hRM3GUdTb4RdXUaZrDHxdlVEGIi2k/XotbAm/k0T5nRSwvDmke/MdItvpdCUQeaRm7AcREXkON/SFTLG0dmCVSNTFFxblAm/rceCoZhXtuWRpbsny1+KuqvYYmXay390HWezVjnN31t6SQUwxXDZXyW3KkQq1CDJBGto+wrsJU7OwOWwiC0aX8vkg3uc4R2j3kgDuPRQoYR4DmukzqR3dVm8d9HF9NsfG12gQCZmSNSSOp2hRwhc4e7AnujzJKvZgswEA5h4kWjU7rY4VwM1COa/K0ajKXHfuIHn6K6hFLZdYEuzn62FzuBs4mBB1EbADxXQcH9knvAL6YYDuZzR3NkfFdtwvhOGpXblzEe8fe+P0C3aFNvl3K6Lul0jF4L7O0qMFrRm/Ubu8jt5Lo6GEhWMc3YK4VR1haIzbsk1inlURUHVNzgrAmolyg6oFEVOiAk+4uf2QxpRury7qqqoPUeHRQCpxJtsk2ybKnDFBJc16sYe5VMYiWMhSQPm71Jrgolo6KQhSBEpipFQcUBW9Z9Z97KeKrCbFDSEJSLAq3NI2VgVdQKrRb/AEDVaQOoXk/tvX/8W9oiGhrbtJ2k6DqV668W6rxb2hrtOJrHMDL3eVyIuR08FKRB7n+H7lTieF06oyvYHCZg3ukkuejVsycT7F4QgwzIerSQQeoGk+SwuI+wlQNHJqZ/8Lxlt3OGiSSkizGxnA6+H9+jI/U3tDvmNPNUsrtbIOa3VoHhb+EySupE8UyrEupHRl/8Fj66fNZVMOLzDTF4JjodT10TpI2WiqZc3CvHvHwgzsNxYafFX0sNmJaGknoATY/LX4pJKJPirRPbNPDcDqkgE5RroXeX6ZkdVt4L2cYLuGY6yev0TJLlllk2XSRq0+HNGgARDKACSSoESLE7WpklCb+RSL2OcNHOHnPzVoxL+vqB+0JJLRTkvZDhEk3GO3DfiPqiaWMG4I+KSS1jkkZygi1uKb1U/wAVTGjgTsJ+qdJbcjJxHDt9/vRM5ySSsQRzKXNSSQE6VREiokkpQHzpB6SSkgcuVb4ITJIAGrheiHfThJJSSuhiUmuSSSiLYHxbFCnSe8mAxrnHrYE/svD6pLnOcbySZ8TOySSMvFaP/9k=",
      //       "data:image/jpeg;base64,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",
      //     ],
      //     highestPrice: 123123,
      //     currentBid: 123123,
      //     startDate: new Date(2021, 10, 10, 12, 10),
      //     endDate: new Date(1995, 11, 17, 3, 24, 0),
      //     bidderNum: 5,
      //   },
      //   {
      //     aid: 2,
      //     status: "S",
      //     bathroomNum: 2,
      //     bedroomNum: 1,
      //     garageNum: 2,
      //     type: "Apartment",
      //     area: 123,
      //     address: "2 Gearin Alley, Mascot, NSW",
      //     photos: [
      //       "data:image/jpeg;base64,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",
      //       "data:image/jpeg;base64,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",
      //     ],
      //     highestPrice: 123123,
      //     currentBid: 123123,
      //     startDate: new Date(2021, 10, 10, 12, 10),
      //     endDate: new Date(1995, 11, 17, 3, 24, 0),
      //     bidderNum: 6,
      //   },
      //   {
      //     aid: 3,
      //     status: "S",
      //     bathroomNum: 2,
      //     bedroomNum: 1,
      //     garageNum: 2,
      //     type: "Apartment",
      //     area: 123,
      //     address: "2 Gearin Alley, Mascot, NSW",
      //     photos: [
      //       "data:image/jpeg;base64,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",
      //       "data:image/jpeg;base64,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",
      //     ],
      //     highestPrice: 123123,
      //     currentBid: 123123,
      //     startDate: new Date(2021, 10, 10, 12, 10),
      //     endDate: new Date(1995, 11, 17, 3, 24, 0),
      //     bidderNum: 7,
      //   },
      //   {
      //     aid: 4,
      //     status: "S",
      //     bathroomNum: 2,
      //     bedroomNum: 1,
      //     garageNum: 2,
      //     type: "Apartment",
      //     area: 123,
      //     address: "2 Gearin Alley, Mascot, NSW 2 Gearin Alley, Mascot, NSW 2 Gearin Alley, Mascot, NSW",
      //     photos: [
      //       "data:image/jpeg;base64,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",
      //       "data:image/jpeg;base64,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",
      //     ],
      //     highestPrice: 123123,
      //     currentBid: 123123,
      //     startDate: new Date(2021, 10, 10, 12, 10),
      //     endDate: new Date(1995, 11, 17, 3, 24, 0),
      //     bidderNum: 5,
      //   },
      // ],
    };
  },

  created() {
    this.username = localStorage.getItem("username");
    // this.username = this.$store.state.username;
    if (this.username !== null) {
      this.hasLogin = true;
      this.avatar = localStorage.getItem("avatar");
      this.$axios
        .get("/notification/unread")
        .then((response) => {
          if (response.data.code === 200) {
            this.unread = response.data.result;
          }
        })
        .catch(function (error) {
          this.$message.error(error);
        });
    }

    if (this.$route.query.postcode === undefined) {
      if (this.$route.query.suburb === undefined) {
        this.address = "";
      } else {
        this.address = this.$route.query.suburb;
        this.suburb = this.$route.query.suburb;
        this.state = this.$route.query.state;
        this.suburbOrPostcode = "suburb";
      }
    } else {
      this.address = this.$route.query.postcode;
      this.postcode = this.$route.query.postcode;
      this.suburbOrPostcode = "postcode";
    }

    this.initialSearch();

    this.currentPage = 1;
    this.originAddress = this.address;
  },

  mounted() {
    $("#back-btn").hover(function (event) {
      $(this).stop().animate({ "margin-left": "10px" }, 300);
      $(this).next(".bottom-line").stop().animate({ width: "100px" }, 300);
    });
    $("#back-btn").mouseleave(function (event) {
      $(this).stop().animate({ "margin-left": "0" }, 300);
      $(this).next(".bottom-line").stop().animate({ width: "0" }, 300);
    });

    $("#back-btn2").hover(function (event) {
      $(this).stop().animate({ "margin-left": "10px" }, 300);
      $(this).next(".bottom-line").stop().animate({ width: "100px" }, 300);
    });
    $("#back-btn2").mouseleave(function (event) {
      $(this).stop().animate({ "margin-left": "0" }, 300);
      $(this).next(".bottom-line").stop().animate({ width: "0" }, 300);
    });
    

    // this.initialSearch();
    var addr = document.getElementById("address");
    addr.value = this.address;

    this.colNumObject.twoColUl = false;
    this.colNumObject.oneColUl = true;
    this.vcardObject.cardWidth40 = true;
    this.vcardObject.cardWidth = false;

    history.pushState("", "Search A Property", "/search");
  },

  methods: {
    ...mapActions(["logout"]),

    openValue() {
      this.show = !this.show;
    },
    getvalue(index, item) {
      this.value = item.name;
      this.show = false;
    },

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
          this.$axios.post("/user/logout").then((response) => {
            if (response.status >= 200 && response.status < 300) {
              if (response.data.code === 200) {
                this.logout();
                this.$router.replace("/");
              } else {
                console.log(response.msg);
              }
            } else {
              console.log(response.msg);
            }
          });
          break;
        default:
          break;
      }
    },

    handleCommand_type(command) {
      this.type = command;
    },

    handleCommand2(command) {
      if (command === "+") {
        this.order = "price";
      }
      if (command === "-") {
        this.order = "-price";
      }
      if (command === "0") {
        this.order = "";
      }
    },

    goto(name) {
      console.log(name);
      this.$router.push({ name: name });
    },
    back() {
      this.$router.go(-1);
    },

    goDetails(item) {
      this.$router.push({
        path: "/auction",
        query: {
          id: item.aid,
        },
      });
    },

    toSearch() {
      this.getProductBySearch();
    },
    showFilter() {
      this.showFilterFlag = !this.showFilterFlag;
    },

    showTime(time) {
      // let time = dayjs(day).format("YYYY-MM-DD HH:mm:ss");
      let MONTH = [
        "Jan",
        "Feb",
        "Mar",
        "Apr",
        "May",
        "Jun",
        "Jul",
        "Aug",
        "Sep",
        "Oct",
        "Nov",
        "Dec",
      ];
      let day = dayjs(time).date();
      let mon = MONTH[dayjs(time).month()];
      let year = dayjs(time).year();
      let hour = dayjs(time).hour();
      let min = dayjs(time).minute();
      let formatTime = `${day} ${mon} ${year}, ${hour}:${min}`;
      return formatTime;
    },

    getBidStatus(item) {
      return item.currentBid;
    },

    checkPropList(val) {
      if (val.length == 1) {
        this.colNumObject.twoColUl = false;
        this.colNumObject.oneColUl = true;
        this.vcardObject.cardWidth60 = true;
        this.vcardObject.cardWidth = false;
      } else {
        this.colNumObject.twoColUl = true;
        this.colNumObject.oneColUl = false;
        this.vcardObject.cardWidth60 = false;
        this.vcardObject.cardWidth = true;
      }
      return;
    },
    getAddressData: function (addressData, placeResultData, id) {
      this.address = addressData;
    },

    handleClear() {
      this.bedrooms = "";
      this.bathrooms = "";
      this.garages = "";
      this.order = "";
      this.minPrice = 0;
      this.maxPrice = 999999999;
      this.minArea = 0;
      this.maxArea = 999999999;
      this.dateFrom = "";
      this.dateTo = "";
      this.pickerOptions = "";
      this.type = "Any";
    },

    initialSearch() {
      this.search = "";

      if (this.postcode === "" && this.suburb === "") {
        this.search = "";
      } else if (this.postcode === "") {
        //initially search by suburb name and state
        this.search = this.search + "suburb=" + this.suburb;
        this.search = this.search + "&state=" + this.state;
      } else {
        this.search = this.search + "postcode=" + this.postcode;
      }

      if (this.search != "") {
        this.search = this.search + "&currPage=1";
      } else {
        this.search = this.search + "currPage=1";
      }

      this.$axios
        .post("/search", this.search)
        .then((res) => {
          this.propList = res.data.result.resVOList;
          // console.log("propList is " + this.propList);
          this.total = res.data.result.totalProp;
          // console.log("total is  ", this.total);
          this.currentPage = 1;
        })
        .catch(function (error) {
          console.log(error);
        });
    },

    createNewSearch() {
      let curr_addr = document.getElementById("address").value;
      if (curr_addr === "") {
        this.tempPostcode = "empty";
        this.tempSuburb = "";
        this.tempState = "";
        this.search = "";
        if (this.showFilterFlag) {
          this.search = this.search + this.createNewFilterQuery();
        }
        this.searchBase = this.search;
        return;
      }

      

      this.search = "";

      if (isNaN(curr_addr)) {

        // fix the bug at created
      let temp1 = curr_addr.split(",");
      if (temp1.length > 1) {
        let temp2 = temp1[0].split(" ");
        let temp3 = temp2.slice(0, temp2.length - 1);
        let temp4 = temp3.join(" ");
        curr_addr = temp4;
      }
        // if curr_addr is text
        if (this.suburb != "") {
          // The original passed address is a suburb
          if (this.suburb === curr_addr) {
            // the input has not changed
            this.search = this.search + "suburb=" + this.suburb;
            this.search = this.search + "&state=" + this.state;
            this.tempPostcode = "";
            this.tempSuburb = this.suburb;
            this.tempState = this.state;
          } else {
            // the input suburb has changed
            if (this.address.locality === undefined) {
              console.log(
                "The curr_addr is " +
                  curr_addr +
                  " and the this.suburb is " +
                  this.suburb
              );
              this.$message.error(
                "Please validate the suburb name by Google Map first!"
              );
              return;
            }
            let new_state = this.address.administrative_area_level_1;
            let new_suburb = this.address.locality;
            this.search = this.search + "suburb=" + new_suburb;
            this.search = this.search + "&state=" + new_state;

            this.tempPostcode = "";
            this.tempSuburb = new_suburb;
            this.tempState = new_state;
          }
        } else {
          //previously it was a postcode, now it is a suburb
          if (this.address.locality === undefined) {
            console.log(
              "The curr_addr is " +
                curr_addr +
                " and the this.suburb is " +
                this.suburb
            );
            this.$message.error(
              "Please validate the suburb name by Google Map first!"
            );
            return;
          }
          let new_state = this.address.administrative_area_level_1;
          let new_suburb = this.address.locality;
          this.search = this.search + "suburb=" + new_suburb;
          this.search = this.search + "&state=" + new_state;
          this.tempPostcode = "";
          this.tempSuburb = new_suburb;
          this.tempState = new_state;
        }
      } else {
        // the curr_addr is postcode
        if (curr_addr.toString().length != 4) {
          this.$message.error("Please input a valid postcode!");
          return;
        }

        console.log("Now the postcode is "+this.postcode);
        this.search = this.search + "postcode=" + this.postcode;
        console.log("Now this.search is " + this.search);
        this.tempPostcode = this.postcode;
        this.tempSuburb = "";
        this.tempState = "";
      }

      if (this.showFilterFlag) {
        this.search = this.search + this.createNewFilterQuery();
      }
      this.searchBase = this.search;
    },

    createNewFilterQuery() {
      let result = "";

      if (this.dateFrom != "") {
        result = result + "&startDate=" + this.dateFrom;
      }

      if (this.dateTo != "") {
        result = result + "&endDate=" + this.dateTo;
      }

      if (this.minPrice != undefined && this.minPrice > 0) {
        let min_price = parseInt(this.minPrice) * 1000;
        result = result + "&minPrice=" + min_price.toString();
      }
      if (this.maxPrice != undefined && this.maxPrice < 100000) {
        let max_price = parseInt(this.maxPrice) * 1000;
        result = result + "&maxPrice=" + max_price.toString();
      }
      if (this.bedrooms !== "") {
        result = result + "&bedrooms=" + this.bedrooms;
      }
      if (this.bathrooms !== "") {
        result = result + "&bathrooms=" + this.bathrooms;
      }
      if (this.garages !== "") {
        result = result + "&garages=" + this.garages;
      }

      if (this.order !== "") {
        result = result + "&order=" + this.order;
      }
      if (this.type !== "Any") {
        result = result + "&propertyType=" + this.type;
      }
      if (this.minArea != undefined && this.minArea > 0) {
        result = result + "&minArea=" + this.minArea;
      }
      if (this.maxArea != undefined && this.maxArea < 100000) {
        result = result + "&maxArea=" + this.maxArea;
      }
      return result;
    },

    // Apply filter now, current page goes back to 1
    getProductBySearch() {
      this.createNewSearch();
      if (this.search != "") {
        this.search = this.search + "&currPage=1";
      } else {
        this.search = "currPage=1";
      }
      console.log("In gpbs now, this.search is "+ this.search);
      this.$axios
        .post("/search", this.search)
        .then((res) => {
          this.propList = res.data.result.resVOList;
          console.log("propList is " + this.propList);
          this.total = res.data.result.totalProp;
          console.log("total is  ", this.total);
          this.currentPage = 1;

          if (this.tempPostcode === "") {
            this.suburbOrPostcode = "suburb";
          } else if ((this.tempPostcode = "empty")) {
            this.suburbOrPostcode = "";
          } else {
            this.suburbOrPostcode = "postcode";
          }

          this.postcode = this.tempPostcode;
          this.suburb = this.tempSuburb;
          this.state = this.tempState;

          this.tempPostcode = "";
          this.tempSuburb = "";
          this.tempState = "";
        })
        .catch(function (error) {
          this.tempPostcode = "";
          this.tempSuburb = "";
          this.tempState = "";
          console.log(error);
        });
    },

    handleCurrentChange: function (currentPage) {
      this.currentPage = currentPage;
      // this.createNewSearch();
      this.search = this.searchBase;
      this.search = this.search + "&currPage=" + this.currentPage;
      this.$axios
        .post("/search", this.search)
        .then((res) => {
          this.propList = res.data.result.propList;
          this.total = res.data.result.total;
        })
        .catch(function (error) {
          console.log(error);
        });
    },

    decapitateAddress(addr) {
      if (addr.length < 30) {
        return addr;
      } else {
        return addr.slice(0, 30) + "...";
      }
    },

    goDetails(item) {
      this.$router.push({
        path: "/auction",
        query: {
          id: item.aid,
        },
      });
    },
  },

  watch: {
    dateFrom: function (newVal, oldVal) {
      if (this.dateFrom === "") {
        this.pickerOptions = "";
        return;
      }

      var date_from = this.dateFrom;
      this.pickerOptions = {
        disabledDate(time) {
          return time.getTime() <= date_from;
        },
      };
    },
  },
};
</script>





<style scoped lang="scss">
@import url("https://fonts.googleapis.com/css2?family=Dancing+Script:wght@700&display=swap");
.user {
  display: flex;
  align-items: center;
  p {
    margin-left: 10px;
  }
}

.back-btn {
  cursor: pointer;
  position: relative;
  /*border:1px solid #123123;*/
}
.back-btn .bottom-line {
  position: absolute;
  /*margin-left:40px;*/
  width: 0;
  height: 2px;
  left: 0;
  bottom: -1px;
  background-color: #3b4c73;
}

#filters {
  margin: 0;
  padding: 0;
  background-color: rgba(59, 124, 221, 0.6);
  width: auto;
  border-radius: 20px;
}

#filter-title {
  margin: 13px;
  margin-left: 15px;
  padding: 0;
  color: white;
  font-family: "Dancing Script", cursive;
}

.items {
  margin: 10px 10%;
  text-align: center;
}

.result-rows {
  margin-top: 20px;
}

.twoColUl {
  column-count: 2;
}

.oneColUl {
  column-count: 1;
}

li {
  margin-left: 100px;
  margin-right: 100px;
  list-style-type: none;
}

.bid {
  width: 90%;
  padding: 10px;
  text-align: center;
  font-size: 18px;
  font-weight: bold;
  color: #fff;
  background-color: #133264;
  border-radius: 3px;
  margin-top: 15px;
}

.comingSoon {
  width: 90%;
  padding: 10px;
  text-align: center;
  font-size: 18px;
  font-weight: bold;
  color: #fff;
  background-color: rgb(216, 158, 15);
  border-radius: 3px;
  margin-top: 15px;
}

.bid2 {
  width: 90%;
  padding: 3px;
  text-align: center;
  font-size: 12px;
  font-weight: bold;
  color: #fff;
  margin-top: 5px;
  background-color: #133264;
  border-radius: 3px;
}

li {
  margin-bottom: 20px;
}

.result-type {
  margin-bottom: 0 !important;
  padding-bottom: 0 !important;
  margin-top: 0 !important;
}

.current-price {
  margin-top: 0 !important;
  margin-bottom: 20px !important;
  padding: 0 !important;
}

.cardWidth40 {
  width: 40vw;
}

.cardWidth {
  width: 35vw;
}

@media only screen and (max-width: 1100px) {
  .twoColUl {
    column-count: 1;
  }

  .cardWidth {
    width: 60vw;
  }
}

.pagination {
  height: 50px;
  text-align: center;
}

.vcard {
  cursor: pointer;
}

.selects {
  .el-select-dropdown__list {
    width: 20% !important;
  }

  .el-scrollbar__view {
    width: 20% !important;
  }

  .el-select-dropdown__item {
    margin: 0 !important;
  }
}

.el-popover {
  width: 5vh !important;
}

.el-scrollbar__view {
  width: 10vh !important;
}

.el-select-dropdown__list {
  width: 5vh !important;
}

.el-select-dropdown__item {
  margin: 0px;
  padding-left: 20px;
}
</style>
