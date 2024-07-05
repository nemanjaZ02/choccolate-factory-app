import { createRouter, createWebHistory } from 'vue-router'
import HomeView from '@/views/HomeView.vue'
import LoginView from '@/views/LoginView.vue'
import RegisterView from '@/views/RegisterView.vue'
import FactoryDetailsView from '@/views/FactoryDetailsView.vue'
import AddChocolateView from '@/views/AddChocolateView.vue'
import UpdateChocolateView from '@/views/UpdateChocolateView.vue'
import AddChocolateFactoryView from '@/views/AddChocolateFactoryView.vue'
import MyCartView from '@/views/MyCartView.vue'
import MyProfileView from '@/views/MyProfileView.vue'
import ManagerPurchasesView from '@/views/ManagerPurchasesView.vue'
import PurchaseDetailsView from '@/views/PurchaseDetailsView.vue'
import CustomerPurchasesView from '@/views/CustomerPurchasesView.vue'
import UsersView from '@/views/UsersView.vue'
import AddEmployeeView from '@/views/AddEmployeeView.vue'
import AddManagerView from '@/views/AddManagerView.vue'

const routes = [
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
      meta: { requiresAuth: true, requiredRole: 'MANAGER' }
    },
    {
      path: '/addChocolateFactoryForm',
      name: 'addChocolateFactoryForm',
      component: AddChocolateFactoryView,
      meta: { requiresAuth: true, requiredRole: 'ADMIN' }
    },
    {
      path: '/updateChocolateForm/:chocolateId',
      name: 'updateChocolateForm',
      component: UpdateChocolateView,
      meta: { requiresAuth: true, requiredRole: 'MANAGER' }
    },
    {
      path: '/myCart/:chocolateId?',
      name: 'myCartView',
      component: MyCartView,
      props: true,
      meta: { requiresAuth: true, requiredRole: 'CUSTOMER' }
    },
    {
      path: '/myProfile',
      name: 'myProfileView',
      component: MyProfileView
    },
    {
      path: '/managerPurchases',
      name: 'managerPurchases',
      component: ManagerPurchasesView,
      meta: { requiresAuth: true, requiredRole: 'MANAGER' }
    },
    {
      path: '/customerPurchases',
      name: 'customerPurchases',
      component: CustomerPurchasesView,
      meta: { requiresAuth: true, requiredRole: 'CUSTOMER' }
    },
    {
      path: '/purchaseDetailsManager/:purchaseId',
      name: 'purchaseDetailsManager',
      component: PurchaseDetailsView,
      props: true,
      meta: { requiresAuth: true, requiredRole: 'MANAGER' }
      
    },
    {
      path: '/purchaseDetailsCustomer/:purchaseId',
      name: 'purchaseDetailsCustomer',
      component: PurchaseDetailsView,
      props: true,
      meta: { requiresAuth: true, requiredRole: 'CUSTOMER' }
      
    },
    {
      path: '/allUsersView',
      name: 'allUsersView',
      component: UsersView,
      meta: { requiresAuth: true, requiredRole: 'ADMIN' }
      
    },
    {
      path: '/addEmployeeView/:factoryId',
      name: 'addEmployeeView',
      component: AddEmployeeView,
      meta: { requiresAuth: true, requiredRole: 'MANAGER' }
      
    }
    ,
    {
      path: '/addManagerView',
      name: 'addManagerView',
      component: AddManagerView,
      meta: { requiresAuth: true, requiredRole: 'ADMIN' }
      
    }
  ];

const router = createRouter({
  history: createWebHistory(import.meta.env.BASE_URL),
  routes
});

router.beforeEach((to, from, next) => {
  const loggedUser = JSON.parse(localStorage.getItem('loggedUser'));
  const isAuthenticated = !!loggedUser;
  
  if (to.matched.some(record => record.meta.requiresAuth)) {
    if (!isAuthenticated) {
      next({ name: 'login' });
    } else {
      const userRole = loggedUser.role;
      if (to.meta.requiredRole && userRole !== to.meta.requiredRole) {
        next({ name: 'home' }); 
      } else {
        next();
      }
    }
  } else {
    next();
  }
});

export default router


