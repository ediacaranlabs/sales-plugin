$(function(){

	$('#signin_form')
	.bootstrapValidator(
			{
				message : '',
				feedbackIcons : {
					valid : 'glyphicon glyphicon-ok',
					invalid : 'glyphicon glyphicon-remove',
					validating : 'glyphicon glyphicon-refresh'
				},
				fields : {
					username : {
						validators : {
							notEmpty : {
								message : messages.signInForm.user_required
							}
						}
					},
					password : {
						validators : {
							notEmpty : {
								message :messages.signInForm.pass_required
							}
						}
					}
				}
			});
	
});
