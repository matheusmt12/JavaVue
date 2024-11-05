import { createRouter, createWebHistory } from 'vue-router';
import HomeView from '../views/HomeView.vue';
import LoginView from '../views/LoginView.vue';
import MarcaView from '@/views/MarcaView.vue';
import CarroView from '@/views/CarroView.vue';
import ClienteView from '@/views/ClienteView.vue';
import ModeloView from '@/views/ModeloView.vue';



const router = createRouter({
  history: createWebHistory(import.meta.env.BASE_URL),
  routes: [
    {
      path: '/',
      name: 'login',
      component: LoginView,
    },
    {
      path: '/about',
      name: 'about',
      // route level code-splitting
      // this generates a separate chunk (About.[hash].js) for this route
      // which is lazy-loaded when the route is visited.
      component: () => import('../views/AboutView.vue'),
    },
    {
      path: '/inicio',
      name: 'home',
      component: HomeView

    },
    {
      path: '/marca',
      name: 'marca',
      component: MarcaView
    },
    {
      path: '/carro',
      name: 'carro',
      component: CarroView
    },
    {
      path: '/cliente',
      name: 'cliente',
      component: ClienteView
    },
    {
      path: '/modelo',
      name: 'modelo',
      component : ModeloView
    }
  ]
})

export default router
