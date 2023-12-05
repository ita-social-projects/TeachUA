require('dotenv').config();

export const ADMIN_EMAIL = process.env.ADMIN_EMAIL;
export const ADMIN_PASSWORD = process.env.ADMIN_PASSWORD;
export const USER_EMAIL = process.env.USER_EMAIL;
export const USER_PASSWORD = process.env.USER_PASSWORD;
export const USER_ROLES = {
    admin: 'admin',
    user: 'user',
  };
export const CITIES = {
    kyiv: "Київ",
    kharkiv: "Харків",
    odessa: "Одеса",
    dnipro: "Дніпро",
    zaporizhzhia: "Запоріжжя",
    noLocation: "Без локації",
}


export const IMAGES_PATH = './src/test/playwright/imagesToUpload/';