export default class View {
    get root() {
        return document.getElementById('root');
    }


    get overlay() {
        return document.getElementById('overlay');
    }

    render() {
        throw new Error('Method not implemented');
    }
}