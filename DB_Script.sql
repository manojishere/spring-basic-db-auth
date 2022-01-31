CREATE TABLE Users (
    id INT AUTO_INCREMENT PRIMARY KEY,
	username VARCHAR(255) NOT NULL,
    password VARCHAR(255) NOT NULL,
    active CHAR(1),
    roles VARCHAR(50)
)

commit;

INSERT INTO `Users`(`username`, `password`, `active`, `roles`) VALUES ('user','pass','Y','ROLE_USER');