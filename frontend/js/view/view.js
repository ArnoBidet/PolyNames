export default class View {
    get root() {
        return document.getElementById('root');
    }

    render() {
        throw new Error('Method not implemented');
    }
}