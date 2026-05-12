insert into user (id, email, first_name, last_name, roles, password) values (1, 'colt@gmail.com', 'Colt', 'Brawler', 'USER', '{bcrypt}$2a$10$zDReSHJiP0DIfAucmf0RnO5RLKjqEeaEKzh3PSueyMG54ku95EfZu');
insert into user (id, email, first_name, last_name, roles, password) values (2, 'jessie@gmail.com', 'Jessie', 'Brawler', 'USER', '{bcrypt}$2a$10$zDReSHJiP0DIfAucmf0RnO5RLKjqEeaEKzh3PSueyMG54ku95EfZu');
insert into profile (id, description , name) values (1, 'Manages everything', 'Admin');
insert into user_profile (id, user_id, profile_id) values (1, 1, 1);
insert into user_profile (id, user_id, profile_id) values (2, 2, 1);