$(function(){

	$('#signup_form')
	.bootstrapValidator(
			{
				message : '',
				feedbackIcons : {
					valid : 'glyphicon glyphicon-ok',
					invalid : 'glyphicon glyphicon-remove',
					validating : 'glyphicon glyphicon-refresh'
				},
				fields : {
					firstName : {
						validators : {
							notEmpty : {
								message : messages.signUpForm.name_empty
							},
							regexp : {
								regexp : regexPatterns.NAME_FORMAT,
								message : messages.signUpForm.name
							},
							stringLength : {
								min : 3,
								max : 60,
								message : messages.signUpForm.name_len
							}

						}
					},
					lastName : {
						validators : {
							notEmpty : {
								message : messages.signUpForm.name_empty
							},
							regexp : {
								regexp : regexPatterns.NAME_FORMAT,
								message : messages.signUpForm.name
							},
							stringLength : {
								min : 3,
								max : 60,
								message : messages.signUpForm.name_len
							}

						}
					},
					password : {
						validators : {
							notEmpty : {
								message : messages.signUpForm.pass_empty
							},
							regexp : {
								regexp : /\*+|[a-zA-z]{1,}/,
								message : messages.signUpForm.pass_char
							},
							regexp : {
								regexp : /\*+|[0-9]{1,}/,
								message : messages.signUpForm.pass_num
							},
							stringLength : {
								min : 12,
								message : messages.signUpForm.pass_len
							}
						}
					},
					email : {
						validators : {
							notEmpty : {
								message : messages.signUpForm.email_empty
							},
							emailAddress : {
								message : messages.signUpForm.email
							}
						}
					},
					c_email : {
						validators : {
							notEmpty : {
								message : messages.signUpForm.email_empty
							},
							identical: {
			                    field: 'email',
			                    message: messages.signUpForm.email
			                }
						}
					},
					address : {
						validators : {
							notEmpty : {
								message : messages.signUpForm.address_empty
							},
							regexp : {
								regexp : regexPatterns.ADDRESS_FORMAT,
								message : messages.signUpForm.address_regex
							},
							stringLength : {
								min : 3,
								max : 60,
								message : messages.signUpForm.address_len
							}

						}
					},
					city : {
						validators : {
							notEmpty : {
								message : messages.signUpForm.city_empty
							},
							regexp : {
								regexp : regexPatterns.ADDRESS_FORMAT,
								message : messages.signUpForm.city_regex
							},
							stringLength : {
								min : 3,
								max : 60,
								message : messages.signUpForm.city_len
							}

						}
					},
					region : {
						validators : {
							regexp : {
								regexp : regexPatterns.NAME_FORMAT,
								message : messages.signUpForm.region_regex
							},
							stringLength : {
								min : 3,
								max : 60,
								message : messages.signUpForm.region_len
							}

						}
					},
					country : {
						validators : {
							notEmpty : {
								message : messages.signUpForm.country_empty
							},
							regexp : {
								regexp : /[A-Z]{3,3}/,
								message : messages.signUpForm.country_regex
							},
							stringLength : {
								min : 1,
								max : 6,
								message : messages.signUpForm.country_len
							}

						}
					},
					zip : {
						validators : {
							notEmpty : {
								message : messages.signUpForm.zip_empty
							},
							regexp : {
								regexp : /^[0-9]{3,}$/,
								message : messages.signUpForm.zip_regex
							},
							stringLength : {
								min : 3,
								max : 60,
								message : messages.signUpForm.zip_len
							}

						}
					},
					phone : {
						validators : {
							regexp : {
								regexp : /^[0-9]+$/,
								message : messages.signUpForm.phone_regex
							},
							stringLength : {
								min : 3,
								max : 16,
								message : messages.signUpForm.phone_len
							}

						}
					},
					
				}
			});
	
});
	