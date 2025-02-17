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

    public Account registerAccount(Account account) {
        if(account.username.length() > 0 && account.password.length() >= 4
                && socialMediaDAO.insertAccount(account)) {
            return socialMediaDAO.getAccountByUsernamePassword(account);
        } else {
            return null;
        }
    }

    public Account loginAccount(Account account) {
        return socialMediaDAO.getAccountByUsernamePassword(account);
    }

    public Message createMessage(Message message) {
        if(message.message_text.length() > 0 && message.message_text.length() <= 255) {
            int message_id = socialMediaDAO.insertMessage(message);
            if(message_id != -1) {
                return socialMediaDAO.getMessageByID(message_id);
            }
        }
        return null;
    }

    public List<Message> getAllMessages() {
        return socialMediaDAO.getAllMessages();
    }

    public Message getMessageByID(int message_id) {
        return socialMediaDAO.getMessageByID(message_id);
    }

    public Message deleteMessageByID(int message_id) {
        Message message = socialMediaDAO.getMessageByID(message_id);
        if(socialMediaDAO.deleteMessageByID(message_id)) {
            return message;
        } else {
            return null;
        }
    }

    public Message updateMessageByID(String message_text, int message_id) {
        Message message = getMessageByID(message_id);
        if(message != null && message_text.length() > 0 && message_text.length() <= 255
                && socialMediaDAO.updateMessageByID(message_text, message_id)) {
            return socialMediaDAO.getMessageByID(message_id);
        } else {
            return null;
        }
    }

    public List<Message> getMessagesByAccountID(int account_id) {
        return socialMediaDAO.getMessagesByAccountID(account_id);
    }
}
