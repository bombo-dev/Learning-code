package com.bombo.chat.server;


import com.bombo.chat.server.command.ChangeNameCommand;
import com.bombo.chat.server.command.Command;
import com.bombo.chat.server.command.GetUesrsCommand;
import com.bombo.chat.server.command.JoinCommand;
import com.bombo.chat.server.command.LeaveCommand;
import com.bombo.chat.server.command.MessageCommand;

import java.util.Optional;

import static com.bombo.chat.common.MyLogger.log;

public class CommandFactory {

    public Command create(String rawCommand) {
        String[] commands = rawCommand.split("\\|");
        CommandSet commandSet = CommandSet.fromString(commands[0])
                .orElseThrow(() -> new IllegalArgumentException("유효하지 않은 명령어입니다."));

        return switch (commandSet) {
            case JOIN -> {
                if (commands.length < 2) {
                    throw new IllegalArgumentException("이름을 입력해주세요.");
                }
                yield JoinCommand.newCommand(commands[1]);
            }
            case MESSAGE -> {
                if (commands.length < 2) {
                    throw new IllegalArgumentException("메세지를 입력해주세요.");
                }
                yield new MessageCommand(commands[1]);
            }
            case CHANGE -> {
                if (commands.length < 2) {
                    throw new IllegalArgumentException("변경할 이름을 입력해주세요.");
                }
                yield new ChangeNameCommand(commands[1]);
            }
            case USERS -> new GetUesrsCommand();
            case EXIT -> new LeaveCommand();
        };
    }

    public enum CommandSet {
        JOIN("join"),
        MESSAGE("message"),
        CHANGE("change"),
        USERS("users"),
        EXIT("exit");

        private final String rawCommand;

        CommandSet(String rawCommand) {
            this.rawCommand = rawCommand;
        }

        public static Optional<CommandSet> fromString(String rawCommand) {
            log("요청 된 명령어 : " + rawCommand);
            for (CommandSet commandSet : CommandSet.values()) {
                if (commandSet.rawCommand.equals(rawCommand)) {
                    return Optional.of(commandSet);
                }
            }
            return Optional.empty();
        }
    }
}
