import { createRouter, createWebHistory } from 'vue-router'
import HomeView from '@/views/HomeView.vue'
import LoginView from '@/views/LoginView.vue'
import RegisterView from '@/views/RegisterView.vue'
import FactoryDetailsView from '@/views/FactoryDetailsView.vue'
import AddChocolateView from '@/views/AddChocolateView.vue'
import UpdateChocolateView from '@/views/UpdateChocolateView.vue'
import AddChocolateFactoryView from '@/views/AddChocolateFactoryView.vue'

const router = createRouter({
  history: createWebHistory(import.meta.env.BASE_URL),
  routes: [
    {
      path: '/',
      name: 'home',
      component: HomeView
    },
    {
      path: '/login',
      name: 'login',
      component: LoginView
    },
    {
      path: '/register',
      name: 'register',
      component: RegisterView,
    },
    {
      path: '/factoryDetails/:factoryId',
      name: 'factoryDetails',
      component: FactoryDetailsView,
      props: true
    },
    {
      path: '/addChocolateForm/:factoryId',
      name: 'addChocolateForm',
      component: AddChocolateView,
    },
    {
      path: '/addChocolateFactoryForm',
      name: 'addChocolateForm',
      component: AddChocolateFactoryView,
    },
    {
      path: '/updateChocolateForm/:chocolateId',
      name: 'updateChocolateForm',
      component: UpdateChocolateView,
    }
  ]
})

export default router


