# bobaos Binding

Starting point for bobaos binding.

First of all, it works with `bobaos@2.0.8` version at least.

It uses `jedis` library to handle PUB/SUB channel communications.

## Supported Things

At this moment is only `Switch`.

## Discovery

There is no auto discovery.

## Thing Configuration

Add a thing, configure required parameters. If you did not change bobaos.pub config.json file, then you should probably leave default values. Check if thing is online. Then go next to adding channels.

## Channels

Add new channel from GUI.

You will be asked to required parameters. For Switch channel is control/status datapoints. Set it to  your numbers, configured in ETS.

## Help required

This is a starting point for binding. Since it is open-source, you may be able to help: implement new channels, add some useful thing, spread the word.

Thanks.
