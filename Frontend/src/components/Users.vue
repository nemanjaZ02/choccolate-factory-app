<template>
    <nav class="navbar navbar-light" style="background-color: transparent; margin-top: 100px;">
        <div class = "container" style="height: 120px; margin-left: 300px; width: 800px;">
            <form class="form-inline" @submit.prevent="search()">
                <div>
                    <label style="font-size: 1.5em; padding: 10px;">Name </label>
                    <input class="form-control mr-sm-2" type="text" step="any" v-model="nameFilter"  placeholder="Search" aria-label="Search" pattern=".*\S.*">
                </div>
                
                <div>
                    <label style="font-size: 1.5em; padding: 10px;">Surname</label>
                    <input class="form-control mr-sm-2" type="text" step="any" v-model="surnameFilter"  placeholder="Search" aria-label="Search" pattern=".*\S.*">
                </div>

                <div>
                    <label style="font-size: 1.5em; padding: 10px;">Username</label>
                    <input class="form-control mr-sm-2" type="text" step="any" v-model="usernameFilter" placeholder="Search" aria-label="Search" pattern=".*\S.*">
                </div>
                <div>
                    <label style="color: transparent; font-size: 1.5em; padding: 10px;">a</label>
                    <button id="searchButton" class="btn btn-primary my-2 my-sm-0"  type="submit">Search</button>
                </div>

                
            </form>        
        </div>
    </nav>
    <div>

        <div class="row" >
            <div class="col-md-2">
                <div class="container" style=" padding: 10px; ">
                    <div style="text-align: center"> 
                        <label style="font-size: 1.5em; padding: 10px;font-weight: bold; text-align: ">FILTERS</label>
                    </div>   
                    <div>
                        <label>Show</label>
                    </div>       
                    <div>
                        <div>
                            <input type="checkbox" group="role" v-on:change="changeSuspiciousFilter(true)">SUSPICIOUS</input>
                        </div>
                        <div style="margin-bottom: 20px;">
                            <input type="checkbox" group="role" v-on:change="changeSuspiciousFilter(false)">NON-SUSPICIOUS</input>
                        </div>
                    </div>        
                    <div>
                        <label>Role</label>
                    </div>
                    <div>
                        <div>
                            <input type="checkbox" group="role" v-on:change="changeRoleFilter('EMPLOYEE')">EMPLOYEE</input>
                        </div>
                        <div>
                            <input type="checkbox" group="role" v-on:change="changeRoleFilter('CUSTOMER')">CUSTOMER</input>
                        </div>
                        <div>
                            <input type="checkbox" group="role" v-on:change="changeRoleFilter('MANAGER')">MANAGER</input>
                        </div>
                        <div style="margin-bottom: 20px;">
                            <input type="checkbox" group="role" v-on:change="changeRoleFilter('ADMIN')">ADMIN</input>
                        </div>
                    </div> 
                    <div>
                        <label>Customer type</label>
                    </div>
                    <div>
                        <div>
                            <input type="checkbox" group="customerType" v-on:change="changeCustomerTypeFilter('Ordinary')">Ordinary</input>
                        </div>
                        
                        <div>
                            <input type="checkbox" group="customerType" v-on:change="changeCustomerTypeFilter('Bronze')">Bronze</input>
                        </div>
                        
                        <div>
                            <input type="checkbox" group="customerType" v-on:change="changeCustomerTypeFilter('Silver')">Silver</input>
                        </div>
                        <div>
                            <input type="checkbox" group="customerType" v-on:change="changeCustomerTypeFilter('Gold')">Gold</input>
                        </div>
                    </div>
                    
                </div>
            </div>
        <div class = "col-md-10" >
            <div style="display: flex; align-items: center;">
                <div style="height: 25px; width: 25px; background: #FFFACD;"></div>
                <label style="margin-left: 12px; margin-top: 8px; font-weight: 500;">- Suspicious users</label>
            </div>
            <div class="container">
                <h1 style="text-align: center;" >Users</h1>
                
                <select class="form-select form-select-lg mb-3" aria-label=".form-select-lg example"  v-on:change="sortUsers()" v-model="sortOption"> 
                    <option disabled selected>Sort By</option>
                    <option value="name_asc">Name Ascending</option>
                    <option value="name_desc">Name Descending</option>
                    <option value="surname_asc">Surname Ascending</option>
                    <option value="surname_desc">Surname Descending</option>
                    <option value="username_asc">Username Ascending</option>
                    <option value="username_desc">Username Descending</option>
                    <option value="points_asc">Customer Points Ascending</option>
                    <option value="points_desc">Customer Points Descending</option>
                </select>
                <label style=" margin-bottom: 10px; margin-left: 10px;" v-if="loggedInUser.factoryId == factory.id && loggedInUser.role == 'ADMIN'">
                    <button v-on:click="showAddManagerForm()"  class="button-with-image">
                        <img src="../../public/Images/addUser.png" style="width: 30px;"  alt="Image">
                    </button>
                </label>
                <div class="row">
                    <div class="col-lg-4 col-md-6 mb-4" v-if="dataLoaded" v-for="u in filteredUsers" :key="u.id"  >
                        <div class="col-md-2">
                        <div :style="{ backgroundColor: u.isSuspicious ? '#FFFACD' : 'white', width: '250px' }" class="card" style="width: 250px;">
                            <div class="card-header" style="font-weight: bold;">
                                {{ u.role }}
                                <button style="margin-left: 20px"  class="button-with-image" v-if="loggedInUser.role=='ADMIN' && u.role!='ADMIN' && !u.isBanned"  v-on:click="banUser(u)">
                                    <img src="../../public/Images/banUser.png" style="width: 35px;"  alt="Image">
                                </button>
                            </div>
                            <div style="padding: 10px">
                                <p style="font-weight: bold;" class="card-text">Name:<label class="card-text" style="margin-left: 10px;">{{ u.name }}</label></p>
                                <p style="font-weight: bold;" class="card-text">Surname:<label class="card-text" style="margin-left: 10px;">{{ u.surname }}</label></p>
                                <p style="font-weight: bold;" class="card-text">Username:<label class="card-text" style="margin-left: 10px;">{{ u.username }}</label></p>
                                <p style="font-weight: bold;" class="card-text">Birthday:<label class="card-text" style="margin-left: 10px;">{{ formatDate(u.birthday) }}</label></p>
                                <p v-if="u.role=='CUSTOMER'" style="font-weight: bold;" class="card-text">Points:<label class="card-text" style="margin-left: 10px;">{{ u.points.toFixed(2) }}</label></p>
                                <p v-if="u.role=='CUSTOMER'" style="font-weight: bold;" class="card-text">Type:<label class="card-text" style="margin-left: 10px;">{{ u.type.typeName }}</label></p>
                                <p v-if="u.isBanned" style="font-weight: bold; color: red;" class="card-text">BANNED</p>
                                <button type="button" v-if="loggedInUser.role=='ADMIN' && u.role!='ADMIN'" class="btn btn-danger" v-on:click="deleteUser(u)">Delete User</button>
                                
                            </div>
                        </div>
                    </div>
                    </div>
                </div>
            </div>
         </div>
        </div>
    </div>
</template>

<script setup>
import axios from 'axios';
import {onMounted, ref} from 'vue';
import {useRouter, useRoute} from 'vue-router';
const route = useRoute();
const router = useRouter();
const loggedInUser = ref({});
const dataLoaded = ref(false);
const factory = ref({name:""})
const users = ref([]);
const filteredUsers = ref([]);
const sortOption = ref("Sort By");
const nameFilter = ref("");
const surnameFilter = ref("");
const usernameFilter = ref("");
const usersRoleFilters = ref([]);
const usersCustomerTypeFilters = ref([]);
const usersSuspiciousFilters = ref([]);
onMounted(()=>{
    loggedInUser.value = JSON.parse(localStorage.getItem('loggedUser'));
    loadUsers();
    
})
function loadUsers() {
    axios.get('http://localhost:8080/ChoccolateAppREST/rest/getAllUsers', {
      headers: {
        'Authorization': `Bearer ${localStorage.getItem('jsonWebToken')}`,
        'Content-Type': 'application/json'
      }
      
    })
    .then(response=>{
        users.value = response.data 
        filteredUsers.value = users.value
      
        if (users.value.length > 0) {
            users.value = users.value.filter(user => !user.isDeleted);
            filteredUsers.value = users.value;
        }
        dataLoaded.value = true;
    });
} 
function formatDate(dateTime) {
    if (dateTime) {
        
        const date = new Date(dateTime);

      
        const options = {
            year: 'numeric',
            month: 'short',
            day: 'numeric',
           
        };

        const formattedDate = date.toLocaleDateString('en-US', options);
        return formattedDate;
    } else {
        return ''; 
    }
}
function sortUsers() {
  if (sortOption.value === "name_asc") {
    filteredUsers.value.sort((a, b) => a.name.localeCompare(b.name));
  } else if (sortOption.value === "name_desc") {
    filteredUsers.value.sort((a, b) => b.name.localeCompare(a.name));
  } else if (sortOption.value === "surname_asc") {
    filteredUsers.value.sort((a, b) => a.surname.localeCompare(b.surname));
  } else if (sortOption.value === "surname_desc") {
    filteredUsers.value.sort((a, b) => b.surname.localeCompare(a.surname));
  } else if (sortOption.value === "username_asc") {
    filteredUsers.value.sort((a, b) => a.username.localeCompare(b.username));
  } else if (sortOption.value === "username_desc") {
    filteredUsers.value.sort((a, b) => b.username.localeCompare(a.username));
  } else if (sortOption.value === "points_desc") {
    filteredUsers.value.sort((a, b) => {
          
            if (a.role === 'CUSTOMER' && b.role !== 'CUSTOMER') {
                return -1; 
            } else if (a.role !== 'CUSTOMER' && b.role === 'CUSTOMER') {
                return 1; 
            } else {
               
                if (a.role === 'CUSTOMER' && b.role === 'CUSTOMER') {
                
                    return b.points - a.points; 
                } else {
                    
                    return a.username.localeCompare(b.username);
                }
            }
        });
  } else if (sortOption.value === "points_asc") {
    filteredUsers.value.sort((a, b) => {
           
            if (a.role === 'CUSTOMER' && b.role !== 'CUSTOMER') {
                return -1; 
            } else if (a.role !== 'CUSTOMER' && b.role === 'CUSTOMER') {
                return 1; 
            } else {
                
                if (a.role === 'CUSTOMER' && b.role === 'CUSTOMER') {
                    return a.points - b.points; 
                } else {
                  
                    return a.username.localeCompare(b.username);
                }
            }
        });
  }
}
function search() {
    filteredUsers.value = users.value.filter(user => user.name.toLowerCase().includes(nameFilter.value.toLowerCase())
     &&  (user.surname.toLowerCase().includes(surnameFilter.value.toLowerCase()))
     &&   (user.username.toLowerCase().includes(usernameFilter.value.toLowerCase())))

    if(usersRoleFilters.value.length > 0) {
        filterByUserRole();
    }  

    if(usersCustomerTypeFilters.value.length > 0) {
        filterByCustomerType();
    } 
    
    if(usersSuspiciousFilters.value.length > 0) {
        filterBySuspiciousFiter();
    }
}
function changeRoleFilter(userRole) {
    if(usersRoleFilters.value.includes(userRole)) {
        usersRoleFilters.value = usersRoleFilters.value.filter(type => type !== userRole);
    }
    else
    {
        usersRoleFilters.value.push(userRole);
    }
    search();
}
function changeCustomerTypeFilter(customerType) {
    if(usersCustomerTypeFilters.value.includes(customerType)) {
        usersCustomerTypeFilters.value = usersCustomerTypeFilters.value.filter(type => type !== customerType);
    }
    else
    {
        usersCustomerTypeFilters.value.push(customerType);
    }
    search();
}
function changeSuspiciousFilter(suspicious) {
    if(usersSuspiciousFilters.value.includes(suspicious)) {
        usersSuspiciousFilters.value = usersSuspiciousFilters.value.filter(filter => filter !== suspicious);
    }
    else
    {
        usersSuspiciousFilters.value.push(suspicious);
    }
    search();
}

function filterByUserRole() {
    let uniqueUsers = new Set();
    usersRoleFilters.value.forEach(role => {
        filteredUsers.value.forEach(user => {
            if (user.role == role ) {
                uniqueUsers.add(user);
            }
        });
    });
    filteredUsers.value = Array.from(uniqueUsers);  
}
function filterByCustomerType() {
    let uniqueUsers = new Set();
    usersCustomerTypeFilters.value.forEach(cType => {
        filteredUsers.value.forEach(user => {
            if(user.role == 'CUSTOMER' ){
                if(user.type.typeName == cType )
                {
                    uniqueUsers.add(user);
                }
            }
                
               
            
        });
    });
    filteredUsers.value = Array.from(uniqueUsers);  
}
function filterBySuspiciousFiter() {
    let uniqueUsers = new Set();
    usersSuspiciousFilters.value.forEach(filter => {
        filteredUsers.value.forEach(user => {
            if (user.isSuspicious == filter) {
                uniqueUsers.add(user);
            }
        });
    });
    filteredUsers.value = Array.from(uniqueUsers);  
}
function showAddManagerForm() {
  router.push('/addManagerView');
}
function deleteUser(user){
    axios.post('http://localhost:8080/ChoccolateAppREST/rest/deleteUser', user.id, {
      headers: {
        'Authorization': `Bearer ${localStorage.getItem('jsonWebToken')}`,
        'Content-Type': 'application/json'
      }
      
    })
    .then(response=>{
        loadUsers();
    });
}
function banUser(user){
    axios.post('http://localhost:8080/ChoccolateAppREST/rest/banUser', user.id, {
      headers: {
        'Authorization': `Bearer ${localStorage.getItem('jsonWebToken')}`,
        'Content-Type': 'application/json'
      }
      
    })
    .then(response=>{
        this.loadUsers();
    });
}

</script>



<style scoped>
.button-with-image {
    display: inline-block;
    background-color: transparent;
    border: none;
    border-radius: 5px;
    cursor: pointer;
    transition: background-color 0.3s ease;
    padding: 10px; 
    margin-right: 20px;
}


  .button-with-image img {
    width: 100%; 
    height: auto; 
}

  .button-with-image:hover {
    background-color: #ddd;
}
.container {
    font-family: Arial, sans-serif;
    background-color: #dfd1c2;
    border-radius: 8px;
    margin-top: 20px;
}
#searchButton {
    background: rgba(123,63,0,1);
    
}

#searchButton:hover {
    background: rgb(83, 43, 0);
}

</style>
