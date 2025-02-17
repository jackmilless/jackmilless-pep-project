package Service;

import Model.Account;
import Model.Message;
import DAO.SocialMediaDAO;

import java.util.List;

public class SocialMediaService {
    SocialMediaDAO socialMediaDAO;

    public SocialMediaService() {
       socialMediaDAO = new 
       SocialMediaDAO();
    }

    /*
     * adds account to database if username and password requirements are met
     * attempts to insert account into database using DAO, failing if username already exists
     * returns inserted Account including account_id or null on failure
     */
    public Account registerAccount(Account account) {
        if(account.username.length() > 0 && account.password.length() >= 4
                && socialMediaDAO.insertAccount(account)) {
            return socialMediaDAO.getAccountByUsernamePassword(account);
        } else {
            return null;
        }
    }

    /*
     * uses DAO to return Account including account_id
     * returns null on failure
     */
    public Account loginAccount(Account account) {
        return socialMediaDAO.getAccountByUsernamePassword(account);
    }

    /* 
     * adds message to database if message meets length requirements
     * returns inserted message or null on failure
     */
    public Message createMessage(Message message) {
        if(message.message_text.length() > 0 && message.message_text.length() <= 255) {
            int message_id = socialMediaDAO.insertMessage(message);
            if(message_id != -1) {
                return socialMediaDAO.getMessageByID(message_id);
            }
        }
        return null;
    }

    /*
     * uses DAO to obtain all messages from database
     * returns empty list if none are found
     */
    public List<Message> getAllMessages() {
        return socialMediaDAO.getAllMessages();
    }

    /*
     * uses DAO to obtain unique message given by message_id from database
     * returns null if not found
     */
    public Message getMessageByID(int message_id) {
        return socialMediaDAO.getMessageByID(message_id);
    }

    /*
     * uses DAO to delete message given by message_id from database 
     * returns presaved message on success or null on failure
     */
    public Message deleteMessageByID(int message_id) {
        Message message = socialMediaDAO.getMessageByID(message_id);
        if(socialMediaDAO.deleteMessageByID(message_id)) {
            return message;
        } else {
            return null;
        }
    }

    /*
     * updates message given by message_id with message_text,
     * assuming message meets length requirements and message exists
     * returns null on failure 
     */
    public Message updateMessageByID(String message_text, int message_id) {
        Message message = getMessageByID(message_id);
        if(message != null && message_text.length() > 0 && message_text.length() <= 255
                && socialMediaDAO.updateMessageByID(message_text, message_id)) {
            return socialMediaDAO.getMessageByID(message_id);
        } else {
            return null;
        }
    }

    /*
     * uses DAO to return all messages posted by Account given by account_id 
     * returns an empty List if none are found 
     */
    public List<Message> getMessagesByAccountID(int account_id) {
        return socialMediaDAO.getMessagesByAccountID(account_id);
    }
}
