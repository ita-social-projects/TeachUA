export const apiUrl = 'http://localhost:3000/dev';
export const clubsUrl = 'http://localhost:3000/dev/clubs';
export const signInUrl = 'http://localhost:8080/dev/api/signin';
export const usersClubs = 'http://localhost:8080/dev/api/clubs';

export const challengesAdminUrl = 'http://localhost:3000/dev/admin/challenges';
export const getChallengesRequest = 'http://localhost:8080/dev/api/challenges';
export const addChallengeAdminUrl = 'http://localhost:3000/dev/admin/addChallenge';

export const tasksAdminUrl = 'http://localhost:3000/dev/admin/tasks';
export const getTasksRequest = 'http://localhost:8080/dev/api/tasks';
export const addTaskAdminUrl = 'http://localhost:3000/dev/admin/addTask';

export const createClubRequest  = {
    url: "http://localhost:8080/dev/api/club",
    method: "POST",
    body: {
        categoriesName: ["Акторська майстерність, театр", "Центр розвитку", "Програмування, робототехніка, STEM"],
        name: "Test Automation club new",
        ageFrom: 10,
        ageTo: 18,
        description:
            '{"blocks":[{"key":"brl63","text":"This is a test description for a new club. Lorem ipsum dolor sit amet, consectetur adipiscing elit, sed do eiusmod tempor incididunt ut labore et dolore magna aliqua. Ut enim ad minim veniam, quis nostrud exercitation ullamco laboris nisi ut aliquip ex ea commodo consequat. Duis aute irure dolor in reprehenderit in voluptate velit esse cillum dolore eu fugiat nulla pariatur. Excepteur sint occaecat cupidatat non proident, sunt in culpa qui officia deserunt mollit anim id est laborum.","type":"unstyled","depth":0,"inlineStyleRanges":[],"entityRanges":[],"data":{}}],"entityMap":{}}',
        userId: "1",
        locations: [
            {
                name: "Automation City",
                cityName: "Дніпро",
                districtName: "Амур-Нижньодніпровський",
                stationName: "Метробудівників",
                address: "Volodymyra Velykoho str.",
                coordinates: "49.790387, 24.028195",
                phone: "0671834956",
                key: 0.112830686973181,
            },
        ],
        urlLogo: "/upload/club/logos/clubLogo.jpg",
        urlBackground: "/upload/club/backgrounds/cover.png",
        urlGallery: ["/upload/club/galleries/galery.jpg"],
        contacts:
            '{"1"::"1236547891","2"::"someFacebook","3"::"someWhatsUp","4"::"testEmail@gmail.com","6"::"testWebPage.com"}',
        centerId: 1,
    },
};

export const createChallengeRequest  = {
    url: "http://localhost:8080/dev/api/challenge",
    method: "POST",
    body: {
        name: "Super Challenge Name",
        description: "This is a test description for a new Challenge. Lorem ipsum dolor sit amet, consectetur adipiscing elit, sed do eiusmod tempor incididunt ut labore et dolore magna aliqua. Ut enim ad minim veniam, quis nostrud exercitation ullamco laboris nisi ut aliquip ex ea commodo consequat. Duis aute irure dolor in reprehenderit in voluptate velit esse cillum dolore eu fugiat nulla pariatur. Excepteur sint occaecat cupidatat non proident.",
        title: "Exquisite test title",
        picture: "/upload/challenges/challenge1.jpg",
        sortNumber: 1122334455
    },
};

export const createTaskRequest  = {
    url: "http://localhost:8080/dev/api/challenge",
    method: "POST",
    body: {
        name: "Super Task Name",
        headerText: "Splendid test Task title. Minimum 40 chars",
        description: "A new TASK test description. Consectetur adipiscing elit, sed do eiusmod tempor incididunt ut labore et dolore magna aliqua. Ut enim ad minim veniam, quis nostrud exercitation ullamco laboris nisi ut aliquip ex ea commodo consequat. Duis aute irure dolor in reprehenderit in voluptate velit esse cillum dolore eu fugiat nulla pariatur. Excepteur sint occaecat cupidatat non proident, sunt in culpa qui officia deserunt mollit anim id est laborum. Lorem ipsum dolor sit amet.",
        startDate: "2024-12-12",
        picture: "/upload/tasks/task1.jpg",
        challengeId: 48
    },
};
export const createTaskRequest2  = {
    url: "http://localhost:8080/dev/api/challenge",
    method: "POST",
    body: {
        name: "Second Task",
        headerText: "Splendid test Task title. Minimum 40 chars",
        description: "A new TASK test description. Consectetur adipiscing elit, sed do eiusmod tempor incididunt ut labore et dolore magna aliqua. Ut enim ad minim veniam, quis nostrud exercitation ullamco laboris nisi ut aliquip ex ea commodo consequat. Duis aute irure dolor in reprehenderit in voluptate velit esse cillum dolore eu fugiat nulla pariatur. Excepteur sint occaecat cupidatat non proident, sunt in culpa qui officia deserunt mollit anim id est laborum. Lorem ipsum dolor sit amet.",
        startDate: "2024-11-19",
        picture: "/upload/tasks/task2.jpg",
        challengeId: 48
    },
};
export const createTaskRequest3  = {
    url: "http://localhost:8080/dev/api/challenge",
    method: "POST",
    body: {
        name: "Third Task",
        headerText: "Splendid test Task title. Minimum 40 chars",
        description: "A new TASK test description. Consectetur adipiscing elit, sed do eiusmod tempor incididunt ut labore et dolore magna aliqua. Ut enim ad minim veniam, quis nostrud exercitation ullamco laboris nisi ut aliquip ex ea commodo consequat. Duis aute irure dolor in reprehenderit in voluptate velit esse cillum dolore eu fugiat nulla pariatur. Excepteur sint occaecat cupidatat non proident, sunt in culpa qui officia deserunt mollit anim id est laborum. Lorem ipsum dolor sit amet.",
        startDate: "2024-11-21",
        picture: "/upload/tasks/task1.jpg",
        challengeId: 48
    },
};
export const createTaskRequest4  = {
    url: "http://localhost:8080/dev/api/challenge",
    method: "POST",
    body: {
        name: "Fourth Task",
        headerText: "Splendid test Task title. Minimum 40 chars",
        description: "A new TASK test description. Consectetur adipiscing elit, sed do eiusmod tempor incididunt ut labore et dolore magna aliqua. Ut enim ad minim veniam, quis nostrud exercitation ullamco laboris nisi ut aliquip ex ea commodo consequat. Duis aute irure dolor in reprehenderit in voluptate velit esse cillum dolore eu fugiat nulla pariatur. Excepteur sint occaecat cupidatat non proident, sunt in culpa qui officia deserunt mollit anim id est laborum. Lorem ipsum dolor sit amet.",
        startDate: "2024-11-22",
        picture: "/upload/tasks/task2.jpg",
        challengeId: 48
    },
};

