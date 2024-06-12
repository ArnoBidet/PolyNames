export const game_id = () => sessionStorage.getItem("game_id");
export const user_id = () => sessionStorage.getItem("user_id");
export const role = () => sessionStorage.getItem("role");

export const set_game_id = (id) => sessionStorage.setItem("game_id", id);
export const set_user_id = (id) => sessionStorage.setItem("user_id", id);
export const set_role = (role) => sessionStorage.setItem("role", role);