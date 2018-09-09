let upPresses = []
let downPresses = []

let getMean = function(arr){

    let sum = 0
    for(let i = 0; i<arr.length; i++){
        sum = sum + arr[i]
    }

    return sum/arr.length
}

let standardize = function(arr){
    let mean = getMean(arr)
    let standardized = []
    for(let i = 0; i < arr.length; i++){
        standardized.push(arr[i]/mean)
    }

    return standardized
}

$('#password').keydown(function(){
        downPresses.push(window.performance.now())
    }
);

$('#password').keyup(function(){
        upPresses.push(window.performance.now())
    }
);

$('#submit_login_data').click(function(){
    let features = []
    for(let i = 0; i < upPresses.length; i++){
        features.push(upPresses[i] - downPresses[i])
        if(i + 1 < upPresses.length){
            features.push(downPresses[i + 1] - upPresses[i])
        }
    }

    $.ajax({
      type: "POST",
      url: '/login',
      data: {
        "name": $('#name').val(),
        "password": $('#password').val(),
        "features": features
      }
    });

    console.log("features: " + features)
});

$('#reset_train_data').click(function(){
    upPresses = []
    downPresses = []
});
