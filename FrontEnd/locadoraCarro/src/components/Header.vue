<script setup>
import router from '@/router';
    import { RouterLink, RouterView } from 'vue-router';

    function logout() {
        localStorage.removeItem('authToken');
        router.push('/')
    }
</script>

<template>
    <header th:fragment="hed">
        <nav class="navbar navbar-expand-lg navbar-light bg-light">
            <a class="navbar-brand" href="#">MT Veículos</a>
            <button class="navbar-toggler" type="button" data-toggle="collapse" data-target="#navbarNavDropdown"
                aria-controls="navbarNavDropdown" aria-expanded="false" aria-label="Toggle navigation">
                <span class="navbar-toggler-icon"></span>
            </button>
            <div class="collapse navbar-collapse" id="navbarNavDropdown">
                <ul class="navbar-nav">
                    <li class="nav-item active">
                        <RouterLink to="/inicio" class="nav-link">Inicio</RouterLink>
                    </li>
                    <li class="nav-item">
                        <RouterLink to="/marca" class="nav-link">Marca</RouterLink>
                    </li>
                    <li class="nav-item">
                        <RouterLink to="/carro" class="nav-link">Carro</RouterLink>
                    </li>
                    <li class="nav-item">
                        <RouterLink to="/cliente" class="nav-link">Cliente</RouterLink>
                    </li>
                    <li class="nav-item dropdown">
                        <a class="nav-link dropdown-toggle" href="#" id="navbarDropdownMenuLink" data-toggle="dropdown"
                            aria-haspopup="true" aria-expanded="false">
                            Configuração
                        </a>
                        <div class="dropdown-menu" aria-labelledby="navbarDropdownMenuLink">
                            <a class="dropdown-item" href="#">Action</a>
                            <a class="dropdown-item" href="#">Another action</a>
                            <a class="dropdown-item" href="#" @click="logout">Logout</a>
                        </div>
                    </li>
                </ul>
            </div>
        </nav>
    </header>
    <RouterView />
</template>