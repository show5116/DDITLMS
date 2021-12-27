(function(){
    const style = document.createElement('style');
    style.id = 'style-context-menu';
    style.type = 'text/css';
    style.innerHTML = [
        'html, body { height: 100%; }',
        '.custom-context-menu {',
        '  z-index: 99999999;',
        '  position: absolute;',
        '  box-sizing: border-box;',
        '  min-height: 100px;',
        '  min-width: 200px;',
        '  background-color: #ffffff;',
        '  box-shadow: 0 0 1px 2px lightgrey;',
        '}',
        '.custom-context-menu ul {',
        '  list-style: none;',
        '  padding: 0;',
        '  background-color: transparent;',
        '}',
        '.custom-context-menu li {',
        '  padding: 3px 5px;',
        '  cursor: pointer;',
        '}',
        '.custom-context-menu li:hover {',
        '  background-color: #f0f0f0;',
        '}'
    ].join('\n');

    const prevStyle = document.getElementById(style.id);
    if( prevStyle ){
        document.head.removeChild( prevStyle );
    }
    document.head.appendChild( style );
})();



// 두개의 Object 결합
// target -> source
function combineProps( sourceProps, targetProps ){
    if( !targetProps ) return sourceProps;

    const combinedProps = {}

    // 기본 옵션과 사용자 옵션 결합
    if( sourceProps instanceof Object && !Array.isArray( sourceProps ) ){
        // source와 target의 key 목록
        const sourceKeys = Object.keys(sourceProps);
        const targetKeys = Object.keys(targetProps);

        // 키 중복 제거
        const propKeys = sourceKeys.concat(targetKeys).reduce(function(prev, crnt, idx){
            const arr = idx === 1 ? [prev] : prev;

            return arr.indexOf( crnt ) === -1 ? arr.concat(crnt) : arr;
        });

        // 결합하기
        propKeys.forEach(function(key){
            const sourceValue = sourceProps[key];
            const targetValue = targetProps[key];

            // 둘다 존재하면 덮어쓰기
            if( targetValue && sourceValue ){
                if ( targetValue instanceof Object ) {
                    if( Array.isArray( targetValue ) ){
                        // 배열이면 Concat
                        combinedProps[key] = [].concat( sourceValue, targetValue );
                    } else {
                        // JSON이면 assign
                        combinedProps[key] = Object.assign({}, sourceValue, targetValue );
                    }
                } else {
                    // Object가 아니면, 그냥 덮어쓰기
                    combinedProps[key] = targetValue;
                }
            } else {
                combinedProps[key] = sourceValue||targetValue;
            }
        });
    }

    return combinedProps;
}

// Context Menu Element 생성
function createElement( props ){
    const element = document.createElement( props.type );

    const keys = Object.keys(props);
    for(let i = 0; i < keys.length; i++){
        const key = keys[i];
        const value = props[key];

        switch( key ){
            case 'visible':
                if( value === false ) return null;
            case 'type':
                break;
            case 'className':
            case 'classList':
                [].concat(value).forEach(function(className){
                    element.classList.add( className );
                })
                break;
            case 'style':
                Object.keys(value).forEach(function(styleName){
                    element[key][styleName] = value[styleName];
                });
                break;
            case 'text':
                element.appendChild( document.createTextNode(value) );
                break;
            case 'onClick':
                if( value instanceof Function ){
                    element.addEventListener('click', value, false);
                }
                break;
            case 'children':
                [].concat(value).map(function( childrenProps ){
                    return createElement( childrenProps );
                }).forEach(function( children ){
                    children && element.appendChild( children );
                });
                break;
            default:
                element[key] = value;
        }
    }

    return element;
}

// Context Menu 렌더링
function renderContextMenu( customProps ){
    // Default Property 설정
    const props = combineProps({
        type: 'div',
        classList: [
            'custom-context-menu',
        ],
    }, customProps);

    // Element 생성
    return createElement( props );
}
/////////////////////////////////////////////////
// Context Menu 생성
    let rightClick = document.querySelectorAll(".right-click");

    rightClick.forEach(right => {
        handleCreateContextMenu(right)
    });
    function handleCreateContextMenu(right){
        right.addEventListener("contextmenu", function (){
            // 기존 Context Menu가 나오지 않게 차단.
            event.preventDefault();
            const target = event.target;
            const imageTarget = target.tagName === "IMG" ? target : target.querySelector('img');
            // const imageTarget = target === "li" ? target : target.querySelectorAll('.right-click a');
            console.log(event.currentTarget);
            var children = right.childNodes;



            // console.log(event.currentTarget.querySelector(""));
            const contextMenu = renderContextMenu({
                id: 'right_context_menu',
                className: 'test',
                style: {
                    top: event.pageY+'px',
                    left: event.pageX+'px',
                },
                children: [
                    {
                        type: 'ul',
                        id: 'menu_list',
                        className: 'menu-list',
                        children: [
                            {
                                type: 'li',
                                className: 'menu-item',
                                children: {
                                    type: 'a',
                                    text: '뒤로가기',
                                    onClick: function( e ){
                                        window.history.back()
                                    },
                                },
                            },
                            {
                                type: 'li',
                                className: 'menu-item',
                                text: '삭제하기',
                                onClick: function (e){
                                    var objectIdx = null;
                                    for(var step=0; step<children.length; step++) {
                                        if (children[step].tagName === "A") {
                                            console.log(children[step].tagName);
                                            console.log(children[step].id);
                                            objectIdx = children[step].id;
                                        }
                                    }
                                    // id값을 구했어 이제 어떡하지 어떡하지어떡하지 지금 이럴때가 아니지 짱돌~!
                                    const params = {
                                        objectIdx : objectIdx
                                    }
                                    $.ajax({
                                        url:"/cloud/deleteObject",
                                        data:params,
                                        type:"POST",
                                        beforeSend: function(xhr){
                                            xhr.setRequestHeader(header,token);
                                        }
                                    }).done(function (fragment){
                                        console.log("전" + fragment);
                                        $("#folderReplace").replaceWith(fragment);
                                        folderMoves = document.querySelectorAll(".folder-move");
                                        folderMoves.forEach(folderMove => replaceEvent(folderMove));
                                        let rightClick = document.querySelectorAll(".right-click");
                                        rightClick.forEach(right => {
                                            handleCreateContextMenu(right)
                                        });

                                    }).fail(function (data, textStatus, errorThrown){
                                        console.log(textStatus);
                                        console.log(errorThrown);
                                        console.log(data);
                                    });
                                }
                            },
                            {
                                // 오른쪽 클릭 대상이 이미지인 경우, 노출
                                visible: !!imageTarget,
                                type: 'li',
                                className: 'menu-item',
                                onClick: function( e ){
                                    // 구글 이미지 검색
                                    const URL = 'https://www.google.co.kr/searchbyimage';
                                    const src = imageTarget.getAttribute('src');
                                    const queryString = "image_url="+src;

                                    console.log( src )

                                    window.open( URL+'?'+queryString );
                                },
                                children: {
                                    type: 'a',
                                    text: "구글 이미지 검색",
                                }
                            }
                        ]
                    }
                ]
            });

            // 이전 Context Menu 삭제, 중복제거
            const prevCtxMenu = document.getElementById( contextMenu.id );
            if( prevCtxMenu ){
                document.body.removeChild(prevCtxMenu);
            }

            // Body에 Context Menu 추가
            document.body.appendChild( contextMenu );
        });
    }

// Context Menu 제거
function handleClearContextMenu(event){
    const contextMenus = document.getElementsByClassName('custom-context-menu');

    [].forEach.call(contextMenus, function(element){
        document.body.removeChild(element);
    });
}

// 이벤트 바인딩
// rightClick.forEach(right => handleCreateContextMenu(right));

// document.addEventListener('contextmenu', handleCreateContextMenu, false);
// document.addEventListener('contextmenu', handleCreateContextMenu, false);
document.addEventListener('click', handleClearContextMenu, false);
