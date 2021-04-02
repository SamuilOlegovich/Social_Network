import Vue from 'vue'
import Vuetify from 'vuetify'
import '@babel/polyfill'
import 'api/resource'
import router from 'router/router'
import App from 'pages/App.vue'
import store from 'store/store'
import { connect } from './util/ws'
import 'vuetify/dist/vuetify.min.css'
import * as Sentry from '@sentry/browser'
import * as Integrations from '@sentry/integrations'

// инициализируем Sentry (логирование)
Sentry.init({
//    dsn: 'https://00b69dd756e94aaf9e9f57e4368a1804@o562961.ingest.sentry.io/5702271',
dsn: "https://bc3cd7223c044e8d83386a797326ec93@o562961.ingest.sentry.io/5702294",
    integrations: [
            new Integrations.Vue({
                Vue,
                attachProps: true,
            }),
        ],
})

// узнаем у какого пользователя что-то происходит
Sentry.configureScope(scope =>
    scope.setUser({
        id: profile && profile.id,
        username: profile && profile.name
    })
)

if (profile) {
    connect()
}

Vue.use(Vuetify)

new Vue({
    el: '#app',
    store,
    router,
    render: a => a(App)
})