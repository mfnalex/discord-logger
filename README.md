Simple Discord bot to log a server's messages to a channel. This is useful if you have lots of channels and never know where a message came from when you hear the annoying discord notification sound.

![img/img.png](img/img.png)

## Building
```bash
./gradlew build
```

## Running

### From command line
Run the .jar using Java 8 or newer and specify your bot token and the channel id you want to log to.
```bash
java -jar discord-logger.jar -b <bot-token> -c <channel-id>
```

### Systemd service
Create a systemd service file, e.g. `/etc/systemd/system/discord-logger.service`:

```ini
[Unit]
Description=Discord Logger
After=network.target

[Service]
Type=simple
User=nobody
group=nogroup
ExecStart=/usr/bin/java -jar <path-to-discord-logger.jar> -b <bot-token> -c <channel-id>
Restart=always
RestartSec=5s
RuntimeMaxSec=86400

[Install]
WantedBy=multi-user.target
```

After that, enable the service:

```bash
systemctl daemon-reload
systemctl start discord-logger
```