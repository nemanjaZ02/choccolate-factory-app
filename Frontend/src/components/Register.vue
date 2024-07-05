 <template>
    <form v-on:click="Register" v-on:submit="registerNewUser($event)">
        <table>
            <tr>
                <td>
                    <label>Username: </label>
                </td>
                <td>
                    <input type="text" v-model="newUser.username" required>
                </td>     
            </tr>
            <tr>
                <td>
                    <label>Password: </label>
                </td>
                <td>
                    <input type="password" v-model="newUser.password" required>
                </td> 
            </tr>
            <tr>
                <td>
                    <label>Re-Type Password: </label> 
                </td>
                <td>
                    <input type="password" v-model="confirmedPassword" required>
                </td> 
                <td>
                    <label v-bind:hidden="confirmedPassword == newUser.password">Password are not matching!</label>
                </td> 
            </tr>
            <tr>
                <td>
                    <label>Name: </label>
                </td>
                <td>
                    <input type="text" v-model="newUser.name" required>
                </td> 
            </tr>
            <tr>
                <td>
                    <label>Surname: </label>
                </td>
                <td>
                    <input type="text" v-model="newUser.surname" required>
                </td> 
            </tr>
            <tr>
                <td>
                    <label>Gender: </label>
                </td>
                <td>
                    <select v-model="newUser.gender" required>
                        <option>MALE</option>
                        <option>FEMALE</option>
                    </select>
                </td> 
            </tr>
            <tr>
                <td>
                    <label>Birthday: </label>
                </td>
                <td>
                    <input type="date" v-model="newUser.birthday" :max="getMaxBirthday()" required>
                </td> 
            </tr>
            <tr>
                <td>
                    <input type="submit" value="Register" v-bind:disabled="!(confirmedPassword == newUser.password)" required>
                </td> 
            </tr>
        </table>
    </form>
    <p style="color: red;">{{ errorMessage }}</p>
</template>

<script setup>
import axios from 'axios';
import { type } from 'jquery';
import { ref } from 'vue';
import { useRouter } from 'vue-router';

const router = useRouter();

const confirmedPassword = ref("");
const newUser = ref({ id: "", username: "", password: "", name: "", surname: "", gender: "", birthday: ""});
const errorMessage = ref('');

function registerNewUser(event) {
	event.preventDefault();
    axios.post("http://localhost:8080/ChoccolateAppREST/rest/register", this.newUser).then(response => {
        router.push('/');
    }).catch(error => {
        errorMessage.value = error.response.data;
        console.error(error);
    });
}

function getMaxBirthday() {
    let today = new Date();

    let dd = String(today.getDate()).padStart(2, '0');
    let mm = String(today.getMonth() + 1).padStart(2, '0');
    let yyyy = today.getFullYear();

    let maxDate = yyyy + '-' + mm + '-' + dd;

    return maxDate;
}
</script>