export const SUCCESS_LOGIN_MESSAGE = 'Ви успішно залогувалися!';

export const CLUB_ONLINE_SLIDER_TOOLTIP = 'Якщо не додано жодної локації буде автоматично онлайн';
export const LOCATION_NAME_TOOLTIP = "Це поле може містити українські та англійські символи довжиною від 5-100. також цифри і спец.символи (!#$%&'()*+,-./:;<=>?@[]^_`{}~)";
export const LOCATION_PHONE_TOOLTIP = "Телефон не може містити літери, спеціальні символи та пробіли";

export const NO_LOCATION_CLUB_ONLINE_MESSAGE = 'Ви не додали жодної локації, гурток автоматично є онлайн';
export const SUCCESS_CLUB_CREATION_MESSAGE = 'Гурток успішно створено';
export const CLUB_ALREADY_EXIST_MESSAGE = 'Гурток з такою назвою вже існує';

//All the error messages should be reviewed once the correct ones has been implemented.
export const ADD_CLUB_VALIDATION_ERROR_MESSAGE = {
    nameIsMandatory: 'Введіть назву гуртка',
    nameIsIncorrect: 'Некоректна назва гуртка',
    categoryIsMandatory: "Це поле є обов'язковим",
    ageIsMandatory: "Вік є обов'язковим",
    phoneIsMandatory: "Введіть номер телефону",
    phoneIsIncorrect: "Телефон не відповідає вказаному формату",
    phoneIsIncorrectCantHaveLetters: "Телефон не може містити спеціальні символи, літери та пробілиТелефон не відповідає вказаному формату",
    emailIsIncorrect: "Некоректний формат email",
    descriptionIsMandatory: "",
    descriptionIsIncorrect: "Некоректний опис гуртка",
    descriptionIsIncorrectCharLimit: "Некоректний опис гурткаОпис гуртка може містити від 40 до 1500 символів.",
}

export const ADD_LOCATION_VALIDATION_ERROR_MESSAGE = {
    locationNameIsMandatory: "Некоректна назва локації",
    locationNameIsIncorrect: "Некоректна назва локації",
    cityIsMandatory: "Це поле є обов'язковим",
    addressIsMandatory: "Це поле є обов'язковим",
    addressIsIncorrect: "Некоректна адреса",
    coordinatesAreIncorrect: "Некоректні координати",
    coordinatesCantHaveLetters: "Некоректні координатиКоординати не можуть містити букви",
    phoneIsMandatory: "Це поле є обов'язковим",
    phoneIsIncorrect: "Телефон не відповідає вказаному формату"
}