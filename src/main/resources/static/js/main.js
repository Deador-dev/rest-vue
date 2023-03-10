function getIndex(list, id) {
    for (let i = 0; i < list.length; i++) {
        if (list[i].id === id) {
            return i;
        }
    }

    return -1;
}

let messageAPI = Vue.resource('/message{/id}');

Vue.component('message-form', {
    props: ['messages', 'messageAttr'],
    data: function () {
        return {
            text: '',
            id: ''
        }
    },
    watch: {
        messageAttr: function (newVal, oldVal) {
            this.text = newVal.text;
            this.id = newVal.id;
        }
    },
    template:
        '<div>' +
        '<input type="text" placeholder="Enter text" v-model="text" />' +
        '<input type="button" value="Save" @click="save" />' +
        '</div>',
    methods: {
        save: function () {
            let message = {text: this.text};

            if (this.id) {
                messageAPI.update({id: this.id}, message).then(result =>
                    result.json().then(data => {
                        let index = getIndex(this.messages, data.id);
                        this.messages.splice(index, 1, data);
                        this.text = '';
                        this.id = '';
                    })
                )
            } else {
                messageAPI.save({}, message).then(result =>
                    result.json().then(data => {
                        this.messages.push(data);
                        this.text = '';
                    })
                );
            }
        }
    }
});

Vue.component('message-row', {
    props: ['message', 'editMethod', 'messages'],
    template:
        '<div>' +
        '<i>({{ message.id }})</i>{{ message.text }}' +
        '<span style="position: absolute; right: 0;">' +
        '<input type="button" value="Edit" @click="edit" />' +
        '<input type="button" value="X" @click="del" />' +
        '</span>' +
        '</div>',
    methods: {
        edit: function () {
            this.editMethod(this.message);
        },
        del: function () {
            messageAPI.remove({id: this.message.id}).then(result => {
                if (result.ok) {
                    this.messages.splice(this.messages.indexOf(this.message), 1)
                }
            })
        }
    }
});

Vue.component('messages-list', {
    props: ['messages'],
    data: function () {
        return {
            message: null
        }
    },
    template:
        '<div style="position: relative; width: 300px;">' +
        '<message-form :messages="messages" :messageAttr="message" />' +
        '<message-row v-for="message in messages" :key="message.id" :message="message" ' +
        ':editMethod="editMethod" :messages="messages" />' +
        '</div>',
    methods: {
        editMethod: function (message) {
            this.message = message;
        }
    }
});


let app = new Vue({
    el: '#app',
    template:
        '<div>' +
            '<div v-if="!profile">Login required <a href="/login">Google</a></div>' +
            '<div v-else>' +
                '<div>{{profile.name}}&nbsp;<a href="/logout">Logout</a></div>' +
                '<messages-list :messages="messages"/>' +
            '</div>' +
        '</div>',
    data: {
        messages: frontendData.messages,
        profile: frontendData.profile
    },
    created: function () {
        // messageAPI.get().then(result =>
        //     result.json().then(data =>
        //         data.forEach(message => this.messages.push(message))
        //     )
        // )
    },
});