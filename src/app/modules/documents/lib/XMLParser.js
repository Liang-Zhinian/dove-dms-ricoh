
function XMLParser() {
    function toJson(xml) {

        // Create the return object
        var obj = {};
        if (xml.type == 1) {// element
            // do attributes
            if (xml.attributes.length > 0) {
                obj["@attributes"] = {};
                for (var j = 0; j < xml.attributes.length; j++) {
                    var attribute = xml.attributes.item(j);
                    obj["@attributes"][attribute.name] = attribute.value;
                }
            }
        } else if (xml.type == 3) {// text
            obj = xml.value;
        }

        // do children
        if (xml.hasChildNodes()) {
            for (var i = 0; i < xml.children.length; i++) {
                var item = xml.children[i];
                var nodeName = item.name.substring(item.name.indexOf(":") + 1).replace('#', '');
                if (typeof (obj[nodeName]) == "undefined") {
                    obj[nodeName] = toJson(item);
                } else {
                    if (typeof (obj[nodeName].push) == "undefined") {
                        var old = obj[nodeName];
                        obj[nodeName] = [];
                        obj[nodeName].push(old);
                    }
                    obj[nodeName].push(toJson(item));
                }
            }
        }
        return obj;
    }

    function parseFromString(xmlText) {
        xmlText = xmlText.replace(/\s{2,}/g, '')
            .replace(/>\s{1,}/g, '>')
            .replace(/\s{1,}</g, '<')
            .replace(/\\t/g, '')
            .replace(/\\n\\r/g, '')
            .replace(/\\n/g, '')
            .replace(/\\r/g, '')
            .replace(/>/g, '>\n')
            .replace(/</g, '\n<');
        var tags = xmlText.split('\n');
        var xml = [];

        for (var i = 0; i < tags.length; i++) {
            if (tags[i] === '') continue;
            if (tags[i].indexOf('?xml') < 0) {
                if (tags[i].indexOf('<') == 0 && tags[i].indexOf('CDATA') < 0) {
                    xml.push(parseTag(tags[i]));
                } else {
                    xml[xml.length - 1].value = parseValue(tags[i]);
                }
            }
        }

        return convertTagsArrayToTree(xml)[0];
    }

    function parseTag(tagText, parent) {
        tagText = tagText.match(/([^\s]*)=["'](.*?)["']|([\/?\w\-\:]+)/g);

        var tag = {
            name: tagText[0],
            attributes: {},
            children: [],
            value: '',
            type: 1,
            getElementsByTagName: function (tagName) {
                var matches = [];

                if (tagName == '*' || this.name.toLowerCase() == tagName.toLowerCase()) {
                    matches.push(this);
                }

                for (var i = 0; i < this.children.length; i++) {
                    matches = matches.concat(this.children[i].getElementsByTagName(tagName));
                }

                return matches;
            },
            hasChildNodes: function () { return this.children.length > 0; }
        };

        for (var i = 1; i < tagText.length; i++) {
            var attribute = tagText[i].split('=');

            tag.attributes[attribute[0]] = typeof attribute[1] === 'string' ?
                attribute[1].replace(/"/g, '').replace(/'/g, '').trim() :
                '';
        }

        tag.attributes.length = Math.max(0, tagText.length - 1);
        tag.attributes.item = function (i) {
            var j = 0;
            for (var key in this) {
                if (j === i) {
                    return { name: key, value: this[key] };
                }
                j++;
            }
        }

        return tag;
    }

    function parseValue(tagValue) {
        if (tagValue.indexOf('CDATA') < 0) {
            return tagValue.trim();
        }

        return tagValue.substring(tagValue.lastIndexOf('[') + 1, tagValue.indexOf(']'));
    }

    function convertTagsArrayToTree(xml) {
        var xmlTree = [];

        if (xml.length == 0) {
            return xmlTree;
        }

        var tag = xml.shift();

        if (tag.value.indexOf('</') > -1) {
            tag.value = tag.value.substring(0, tag.value.indexOf('</'));
            xmlTree.push(tag);
            xmlTree = xmlTree.concat(convertTagsArrayToTree(xml));

            return xmlTree;
        }

        if (tag.name.indexOf('/') == 0) {
            return xmlTree;
        }                

        xmlTree.push(tag);
        tag.children = convertTagsArrayToTree(xml);

        if (tag.children.length > 0) tag.type = 1;
        else tag.type = 3;
        
        xmlTree = xmlTree.concat(convertTagsArrayToTree(xml));

        return xmlTree;
    }

    function toString(xml) {
        var xmlText = convertTagToText(xml);

        if (xml.children.length > 0) {
            for (var i = 0; i < xml.children.length; i++) {
                xmlText += toString(xml.children[i]);
            }

            xmlText += '</' + xml.name + '>';
        }

        return xmlText;
    }

    function convertTagToText(tag) {
        var tagText = '<' + tag.name;
        var attributesText = [];

        for (var attribute in tag.attributes) {
            tagText += ' ' + attribute + '="' + tag.attributes[attribute] + '"';
        }

        if (tag.value.length > 0) {
            tagText += '>' + tag.value + '</' + tag.name + '>';
        } else {
            tagText += '>';
        }

        return tagText;
    }

    return {
        parseFromString: parseFromString,
        toJson: toJson,
        toString: toString
    };
}

export default XMLParser;